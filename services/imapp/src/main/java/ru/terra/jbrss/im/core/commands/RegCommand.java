package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.constants.ContactStatus;
import ru.terra.jbrss.db.entity.Contact;
import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@IMCommand(value = "reg", help = "Reg command, syntax: reg login password", needAuth = false)
public class RegCommand extends AbstractCommand {

    private enum MathExpr {
        PLUS, MINUS, ADD;

        public static MathExpr valueOf(int exprId) {
            switch (exprId) {
                case 0:
                    return PLUS;
                case 1:
                    return MINUS;
                case 2:
                    return ADD;
            }
            return PLUS;
        }
    }

    @Override
    public boolean doCmd(String contact, List<String> params) {
        if (serverInterface.isContactExists(contact)) {
            sendMessage("You already registered");
            return false;
        } else {
            if (params.size() < 1) {
                sendMessage("Not enogh params");
            } else {
                Contact c = serverInterface.getContact(contact);
                if (c != null && c.getStatus() == ContactStatus.SENT_QUESTION.ordinal()) {
                    if (c.getCorrectAnswer().equals(params.get(0))) {
                        serverInterface.sendMessage(contact, "Answer is correct, registration complete");
                        c.setStatus(ContactStatus.READY.ordinal());
                        serverInterface.updateContact(c);
                        return true;
                    } else {
                        serverInterface.sendMessage(contact, "Answer is not correct");
                        return false;
                    }
                } else {
                    String question = "Please enter correct answer for mathematical expression: ";
                    MathExpr expNum = MathExpr.valueOf(ThreadLocalRandom.current().nextInt(0, 3));
                    Integer fp = ThreadLocalRandom.current().nextInt(0, 11);
                    question += fp.toString();
                    Integer sp = ThreadLocalRandom.current().nextInt(0, 11);
                    Integer answer = 0;
                    switch (expNum) {
                        case PLUS:
                            question += "+";
                            answer = fp + sp;
                            break;
                        case MINUS:
                            question += "-";
                            answer = fp - sp;
                            break;
                        case ADD:
                            question += "*";
                            answer = fp * sp;
                            break;
                    }
                    question += sp.toString();
                    serverInterface.sendMessage(contact, question);
                    serverInterface.regContact(contact, answer, params.get(0), UUID.randomUUID().toString());
                }
            }
        }
        return true;
    }
}
