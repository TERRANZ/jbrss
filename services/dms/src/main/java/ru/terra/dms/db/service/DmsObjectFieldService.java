package ru.terra.dms.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.terra.dms.constants.DmsConstants;
import ru.terra.dms.db.entity.DmsObject;
import ru.terra.dms.db.entity.DmsObjectField;
import ru.terra.dms.db.repos.DmsObjectFieldRepo;
import ru.terra.dms.dto.DmsObjectFieldDto;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class DmsObjectFieldService {

    @Autowired
    private DmsObjectFieldRepo repostory;

    public Optional<DmsObjectField> findOne(final String id) {
        if (id == null) {
            return null;
        }
        return Optional.ofNullable(repostory.findOne(id));
    }

    public DmsObjectFieldDto mapDto(final DmsObjectField of, final String objectId) {
        final DmsObjectFieldDto ret = new DmsObjectFieldDto(
                of.getId(),
                of.getCreatedDate(),
                of.getModifiedDate(),
                objectId,
                of.getType().name(),
                of.getName()
        );
        switch (of.getType()) {
            case STRING:
                ret.setStrVal(of.getStrval());
                break;
            case INT:
                ret.setIntVal(of.getIntval());
                break;
            case LONG:
                ret.setLongVal(of.getLongval());
                break;
            case FLOAT:
                ret.setFloatVal(of.getFloatval());
                break;
            case DATE:
                ret.setDateVal(of.getDateval().getTime());
                break;
            case LIST:
                ret.setListVal(of.getListval());
                break;
        }
        return ret;
    }

    public DmsObjectField mapEntity(final DmsObjectFieldDto dto, final DmsObject dmsObject) {
        final DmsObjectField ret = findOne(dto.getId()).orElseGet(() -> new DmsObjectField(dmsObject, dto.getName(), DmsConstants.DmsTypes.valueOf(dto.getType())));

        ret.setModifiedDate(LocalDateTime.now());
        switch (ret.getType()) {
            case STRING:
                ret.setStrval(dto.getStrVal());
                break;
            case INT:
                ret.setIntval(dto.getIntVal());
                break;
            case LONG:
                ret.setLongval(dto.getLongVal());
                break;
            case FLOAT:
                ret.setFloatval(dto.getFloatVal());
                break;
            case DATE:
                ret.setDateval(new Date(dto.getDateVal()));
                break;
            case LIST:
                ret.setListval(dto.getListVal());
                break;
        }

        return ret;
    }
}
