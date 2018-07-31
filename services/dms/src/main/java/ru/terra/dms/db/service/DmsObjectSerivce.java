package ru.terra.dms.db.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.terra.dms.db.entity.DmsObject;
import ru.terra.dms.db.repos.DmsObjectRepo;
import ru.terra.dms.dto.DmsObjectDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DmsObjectSerivce {
    @Autowired
    private DmsObjectRepo repository;

    @Autowired
    private DmsObjectFieldService dmsObjectFieldService;

    public List<DmsObjectDto> findAll() {
        return repository.findAll().parallelStream().map(this::mapDto).collect(Collectors.toList());
    }

    public Optional<DmsObject> findOne(final String id) {
        if (id == null) {
            return null;
        }
        return Optional.ofNullable(repository.findOne(id));
    }

    public DmsObjectDto mapDto(final DmsObject o) {
        if (o == null) {
            return null;
        }
        return new DmsObjectDto(
                o.getId(),
                o.getCreatedDate(),
                o.getModifiedDate(),
                o.getFieldList().parallelStream().map(of ->
                        dmsObjectFieldService.mapDto(of, o.getId())
                ).collect(Collectors.toList())
        );
    }

    public DmsObject mapEntity(final DmsObjectDto dto, final String currentUser) {
        final DmsObject ret = findOne(dto.getId()).orElseGet(() -> {
            final DmsObject obj = new DmsObject(currentUser, Lists.newArrayList());
            obj.setId(dto.getId());
            obj.setUserId(currentUser);
            return obj;
        });
        ret.setModifiedDate(LocalDateTime.now());
        if (dto.getField() != null && dto.getField().size() > 0) {
            ret.getFieldList().addAll(
                    dto.getField()
                            .parallelStream()
                            .map(fieldDto -> dmsObjectFieldService.mapEntity(fieldDto, ret)).collect(Collectors.toList())
            );
        }
        return ret;
    }

    public void save(final DmsObjectDto dto, final String currentUser) {
        repository.save(mapEntity(dto, currentUser));
    }

    public void delete(final DmsObject dmsObject) {
        repository.delete(dmsObject);
    }

    public void update(final DmsObject dmsObject, final DmsObjectDto dto) {

    }

    public List<DmsObject> findByParam(final String param, final String val) {
        return null;
    }
}
