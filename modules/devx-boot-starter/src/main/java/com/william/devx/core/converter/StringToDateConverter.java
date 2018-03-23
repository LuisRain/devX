package com.william.devx.core.converter;

import com.william.devx.common.$;
import com.william.devx.common.date.DateFormatStyle;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;
import java.util.Objects;

/**
 * 这里只能对普通参数进行转换
 * Created by sungang on 2018/3/20.
 */
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        Date date = null;
        for (DateFormatStyle formatStyle : DateFormatStyle.values()) {
            date = $.date.formatStringByStyle(source.trim(), formatStyle.getDateStyle());
            if (Objects.nonNull(date)) {
                break;
            }
        }
        return date;
    }

}
