package com.whatachad.app.util;

import com.whatachad.app.common.CommonException;
import com.whatachad.app.common.IError;

import java.lang.reflect.Field;
import java.util.Objects;

public class EntityUtils {

    /**
     * note :
     * dto에 저장된 필드 값이 null이 아닐 경우 값을 변경한다.
     * entity의 필드명과 dto의 필드명을 동일하게 유지할 때 정상적으로 동작한다.
     * @param entity
     * @param dto
     * @param <T>
     * @param <S>
     */
    public static <T, S> void setValueExceptNull(T entity, S dto) {
        try {
            Class<?> entityClass = entity.getClass();
            Class<?> dtoClass = dto.getClass();
            for (Field dtoField : dtoClass.getDeclaredFields()) {
                Field entityField = entityClass.getDeclaredField(dtoField.getName());
                entityField.setAccessible(true);
                dtoField.setAccessible(true);
                if (Objects.nonNull(dtoField.get(dto))) {
                    entityField.set(entity, dtoField.get(dto));
                }
            }
        } catch (IllegalAccessException e) {
            throw new CommonException(IError.FIELD_NOT_ALLOWED);
        } catch (NoSuchFieldException e) {
            throw new CommonException(IError.FIELD_NOT_EXIST);
        }
    }

    public static <T, S> void setEntity(String fieldName, T source, S target) {
        try {
            Class<?> sourceClass = source.getClass();
            Field field = sourceClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(source, target);
        } catch (IllegalAccessException e) {
            throw new CommonException(IError.FIELD_NOT_ALLOWED);
        } catch (NoSuchFieldException e) {
            throw new CommonException(IError.FIELD_NOT_EXIST);
        }
    }
}
