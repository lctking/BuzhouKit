package com.lctking.buzhoukitidempotent.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpELParser {
    public static Object parse(String spEL, Method method, Object[]args){
        //get parameters of the method
        String[] parameterNames = new DefaultParameterNameDiscoverer().getParameterNames(method);
        //convert spEL:String to expression:Expression
        Expression expression = new SpelExpressionParser().parseExpression(spEL);

        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        if(parameterNames != null){
            // collect key-value to map
            Map<String, Object> maps = IntStream.range(0, args.length)
                    .boxed()
                    .collect(Collectors.toMap(
                            i -> parameterNames[i],
                            i -> args[i],
                            (exist, replace) -> replace
                    ));
            standardEvaluationContext.setVariables(maps);
        }
        return expression.getValue(standardEvaluationContext);
    }
}
