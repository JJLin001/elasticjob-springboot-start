package com.taotao.nanotation;

import com.taotao.autoconfigure.JobParserAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/16/18:04
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({JobParserAutoConfiguration.class})
public @interface EnableElasticJob {
}
