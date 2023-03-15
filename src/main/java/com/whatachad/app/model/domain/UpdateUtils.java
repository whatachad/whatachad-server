package com.whatachad.app.model.domain;

import com.whatachad.app.common.CommonException;
import com.whatachad.app.common.IError;

import java.lang.reflect.Field;

public class UpdateUtils {

    /**
     * 변경 가능한 필드 이름
     */
    public static final String FACILITY_ADDRESS = "address";

    /**
     * <변경될 필드에 대해서만 필드 수정하는 메서드>
     * @param object
     * @param fieldName
     * @param newValue
     * @param <T>
     */
    // TODO : 변경되지 말아야 할 필드에 대한 안전성 검증 필요
    public static <T> void changeField(T object, String fieldName, Object newValue) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, newValue);
        } catch (NoSuchFieldException e) {
            throw new CommonException(IError.FIELD_NOT_EXIST);
        } catch (IllegalAccessException e) {
            throw new CommonException(IError.FIELD_NOT_ALLOWED);
        }
    }
}
