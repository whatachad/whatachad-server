package com.whatachad.app.util;

import com.whatachad.app.common.CommonException;
import com.whatachad.app.common.IError;
import jakarta.persistence.Embeddable;

import java.lang.reflect.Field;
import java.util.Objects;

public class EntityUtil {

    /**
     * note :
     * dto에 저장된 필드 값이 null이 아닐 경우 값을 변경한다.
     * entity의 필드명과 dto의 필드명을 동일하게 유지할 때 정상적으로 동작한다.
     * @param target
     * @param source
     * @param <T>
     * @param <S>
     */
    public static <T, S> void setValueExceptNull(T target, S source) {
        try {
            Class<?> targetClass = target.getClass();
            Class<?> sourceClass = source.getClass();
            for (Field sourceField : sourceClass.getDeclaredFields()) {
                Field targetField = targetClass.getDeclaredField(sourceField.getName());
                targetField.setAccessible(true);
                sourceField.setAccessible(true);
                Object fieldValue = sourceField.get(source);
                if (Objects.nonNull(fieldValue)) {
                    if (fieldValue.getClass().isAnnotationPresent(Embeddable.class)) {
                        fieldValue = getValueObjectExceptNull(targetField.get(target), fieldValue);
                    }
                    targetField.set(target, fieldValue);
                }
            }
        } catch (IllegalAccessException e) {
            throw new CommonException(IError.FIELD_NOT_ALLOWED);
        } catch (NoSuchFieldException e) {
            throw new CommonException(IError.FIELD_NOT_EXIST);
        }
    }

    public static <T, S> void setEntity(String fieldName, T target, S source) {
        try {
            Class<?> sourceClass = target.getClass();
            Field field = sourceClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, source);
        } catch (IllegalAccessException e) {
            throw new CommonException(IError.FIELD_NOT_ALLOWED);
        } catch (NoSuchFieldException e) {
            throw new CommonException(IError.FIELD_NOT_EXIST);
        }
    }

    private static <T, S> Object getValueObjectExceptNull(T target, S source) {
        try {
            Class<?> targetClass = target.getClass();
            Class<?> sourceClass = source.getClass();
            for (Field sourceField : sourceClass.getDeclaredFields()) {
                Field targetField = targetClass.getDeclaredField(sourceField.getName());
                targetField.setAccessible(true);
                sourceField.setAccessible(true);
                Object fieldValue = sourceField.get(source);
                if (Objects.nonNull(fieldValue)) {
                    targetField.set(target, fieldValue);
                }
            }
            return target;
        } catch (IllegalAccessException e) {
            throw new CommonException(IError.FIELD_NOT_ALLOWED);
        } catch (NoSuchFieldException e) {
            throw new CommonException(IError.FIELD_NOT_EXIST);
        }
    }
}
