package com.artlongs.framework.utils;


import com.trigersoft.jaque.expression.Expression;
import com.trigersoft.jaque.expression.InvocationExpression;
import com.trigersoft.jaque.expression.LambdaExpression;
import com.trigersoft.jaque.expression.MemberExpression;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.UnderlinedNameConversion;

import java.io.Serializable;
import java.lang.reflect.Member;
import java.util.function.Function;

import static com.artlongs.framework.utils.Qe.Opt.GROUP;

/**
 * FUNC: Lambda 风格的 Query Expression.
 * User: leeton
 * Date: 3/14/18
 * Time: 2:33 PM
 */
public class Lq<T> extends Qe<T> {

    public Lq(){}
    public Lq(Class<T> clz) {
       super(clz);
    }

    public Lq(Class<T> clz,SQLManager sqlManager) {
        super(clz, sqlManager);
    }
    
    public interface Property<T, R> extends Function<T, R>, Serializable { }

    private String getFunctionName(Property<T, ?> fun) {
        LambdaExpression parsed = LambdaExpression.parse(fun);
        Expression body = parsed.getBody();
        Member member = ((MemberExpression) ((InvocationExpression) body).getTarget()).getMember();
        String method = member.getName();
        String attr = null;
        if (method.startsWith("get")) {
            attr = method.substring(3);
        } else {
            attr = method.substring(2);
        }
        return new UnderlinedNameConversion().getColName(super.clz, attr);
    }

    private String[] getFunctionName(Property<T, ?>... funs) {
        String[] cols = new String[funs.length];
        int i = 0;
        for (Property<T, ?> fun : funs) {
            cols[i++] = this.getFunctionName(fun);
        }
        return cols;

    }

    public Lq<T> select(Property<T, ?>... fun) {
        super.select(getFunctionName(fun));
        return this;
    }

    public static Lq del(Class clz) {
        Lq qe = new Lq();
        qe.sql.append(Opt.DEL.of(getTableName(clz)));
        return qe;
    }


    public Lq<T> where(Lq qe) {
        super.where(qe);
        return this;
    }

    public Lq<T> leftJoin(Class joinTableClass,Property<T, ?>joinKey,Property<T, ?> mainKey) {

        return  (Lq<T>) super.leftJoin(joinTableClass, getFunctionName(joinKey),getFunctionName(mainKey));
    }

    public Lq<T> eq(Property<T, ?>fun,Object val) {
        super.eq(getFunctionName(fun),val);
        return this;
    }

    public Lq ne(Property<T, ?>fun,Object val) {
        super.ne(getFunctionName(fun),val);
        return this;
    }

    public Lq lt(Property<T, ?>fun,Object val) {
        super.lt(getFunctionName(fun),val);
        return this;
    }

    public Lq gt(Property<T, ?>fun,Object val) {
        super.gt(getFunctionName(fun),val);
        return this;
    }

    public Lq le(Property<T, ?>fun,Object val) {
        super.le(getFunctionName(fun),val);
        return this;
    }

    public Lq ge(Property<T, ?>fun,Object val) {
        super.ge(getFunctionName(fun),val);
        return this;
    }

    public Lq in(Property<T, ?>fun,Object val) {
        super.in(getFunctionName(fun),val);
        return this;
    }

    public Lq between(Property<T, ?>fun, Object left ,Object right) {
        super.between(getFunctionName(fun),left,right);
        return this;
    }

    public Lq isnull(Property<T, ?>fun) {
        super.isnull(getFunctionName(fun));
        return this;
    }

    public Lq notnull(Property<T, ?>fun) {
        super.notnull(getFunctionName(fun));
        return this;
    }

    public Lq like(Property<T, ?>fun,Object v1 ) {
        super.like(getFunctionName(fun),v1);
        return this;
    }

    public Lq and(Lq qe) {
        super.and(qe);
        return this;
    }

    public Lq or(Lq qe) {
        super.or(qe);
        return this;
    }

    public Lq asc(Property<T, ?>fun) {
        super.asc(getFunctionName(fun));
        return this;
    }
    public Lq desc(Property<T, ?>fun) {
        super.desc(getFunctionName(fun));
        return this;
    }

    public String limit(Integer l, Integer r) {
        return super.limit(l, r);
    }

    public Lq group(Property<T, ?>... funs) {
        super.group(getFunctionName(funs));
        return this;
    }

    public Qe having(Property<T, ?>fun,Opt opt,Object val) {
        super.having(getFunctionName(fun), opt, val);
        return this;
    }

}
