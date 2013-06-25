package ru.terra.twochsaver.twoch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.web.bind.annotation.RequestParam;

import ru.terra.jbrss.model.Model;
import ru.terra.jbrss.util.RequestUtil;
import ru.terra.twochsaver.twoch.db.TMessage;
import ru.terra.twochsaver.twoch.db.TThread;
import ru.terra.twochsaver.twoch.db.controller.TMessageJpaController;
import ru.terra.twochsaver.twoch.db.controller.TThreadJpaController;
import ru.terra.twochsaver.twoch.db.controller.exceptions.PreexistingEntityException;
import ru.terra.twochsaver.twoch.dto.BoardDTO;
import ru.terra.twochsaver.twoch.dto.PostDTO;
import ru.terra.twochsaver.twoch.dto.SingleThreadDTO;
import ru.terra.twochsaver.twoch.dto.ThreadDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 * 
 * @author terranz
 */
public class WorkerThread implements Runnable {
	public final static String SERVER_URL = "http://2ch.pm/";
	private String boardName;
	private final static String WAKABA_URL = "wakaba.json";
	private final static String JSON = ".json";
	private WorkIsDoneListener wdl;
	private Boolean isDownloadImages;	
	
	@Inject
	private Model model;

	public WorkerThread(String board, WorkIsDoneListener wd, Boolean downloadImages) {
		this.boardName = board;
		this.wdl = wd;
		isDownloadImages = downloadImages;		
	}

	@Override
	public void run() {
		try {
			String result = "";
			URLConnection conn = new URL(SERVER_URL + boardName + WAKABA_URL).openConnection();
			String json = RequestUtil.readStreamToString(conn.getInputStream(), "UTF-8");
			// System.out.println("Received json: " + json);
			BoardDTO brd = new JSONDeserializer<BoardDTO>().deserialize(json, BoardDTO.class);
			if (brd != null) {
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("2chPU");
				TThreadJpaController tpm = new TThreadJpaController(emf);
				TMessageJpaController mpm = new TMessageJpaController(emf);
				// F//ileWriter fstream = new FileWriter("images.txt", true);
				// BufferedWriter out = new BufferedWriter(fstream);
				ExecutorService service = Executors.newFixedThreadPool(5);
				for (ThreadDTO ts : brd.threads) {
					for (PostDTO m : ts.posts.get(0)) {
						//System.out.println("thread: " + m.num);
						result += "thread: " + m.num + "\n";
						// firwst message in thread - num of thread

						TThread t = tpm.findByStartMessage(m.num);
						if (t == null) {
							t = new TThread(0L, m.num, boardName);
							tpm.create(t);
						}						
						conn = new URL(SERVER_URL + boardName + "/res/" + m.num.toString() + JSON).openConnection();
						json = RequestUtil.readStreamToString(conn.getInputStream(), "UTF-8");
						SingleThreadDTO tdto = new JSONDeserializer<SingleThreadDTO>().deserialize(json, SingleThreadDTO.class);
						for (List<PostDTO> msgsInThread : tdto.thread) {
							PostDTO msg = msgsInThread.get(0);
							//System.out.println("Message : " + msg.num);
							if (!mpm.isMessageExists(msg.num)) {
								result += "Message " + msg.num + " for thread " + m.num + " cached" + "\n";
								mpm.create(new TMessage(msg));
								if (msg.image != null) {
									String url = SERVER_URL + boardName + msg.image;
									if (isDownloadImages) {
										service.submit(new DownloaderThread(SERVER_URL + boardName + msg.image, m.num.toString()));

									}
								}
							}
						}
					}
				}
				service.shutdown();
				// out.close();
				// fstream.close();
				if (wdl != null) {
					wdl.done(result);
				} else {
					System.out.print(result);
				}
			} else {
				System.out.println("board is null");
			}
		} catch (IOException ex) {
			Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
		} catch (PreexistingEntityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
}
