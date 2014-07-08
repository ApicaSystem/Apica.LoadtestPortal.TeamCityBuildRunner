/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author andras.nemes
 */
public class ListUtils
{

    public static String join(Collection<String> lst, String separator)
    {
        StringBuilder buf = new StringBuilder(lst.size() * 64);
        boolean first = true;
        for (String value : lst)
        {
            if (first)
            {
                first = false;
            } else
            {
                buf.append(separator);
            }
            buf.append(value);
        }
        return buf.toString();
    }

    public static <T> T last(List<T> lst)
    {
        if ((lst == null) || (lst.isEmpty()))
        {
            return null;
        }
        return lst.get(lst.size() - 1);
    }

    public static <From, To> List<To> map(List<From> list, MapClosure<From, To> f)
    {
        List<To> result = new ArrayList(list.size());
        for (From value : list)
        {
            result.add(f.eval(value));
        }
        return result;
    }

    public static <Accumulator, Value> Accumulator reduce(List<Value> list, Accumulator init, ReduceClosure<Accumulator, Value> f)
    {
        Accumulator accumulator = init;
        for (Value value : list)
        {
            accumulator = f.eval(accumulator, value);
        }
        return accumulator;
    }

    public static double average(List<? extends Number> values)
    {
        if ((values == null) || (values.isEmpty()))
        {
            return 0.0D;
        }
        double sum = 0.0D;
        Number v;
        for (Iterator i$ = values.iterator(); i$.hasNext(); sum += v.doubleValue())
        {
            v = (Number) i$.next();
        }
        return sum / values.size();
    }

    public static int median(List<Integer> values)
    {
        if ((values == null) || (values.isEmpty()))
        {
            return 0;
        }
        values = new ArrayList(values);
        Collections.sort(values);

        int size = values.size();
        int sizeHalf = size / 2;
        if (size % 2 == 1)
        {
            return ((Integer) values.get(sizeHalf)).intValue();
        }
        return (((Integer) values.get(sizeHalf - 1)).intValue() + ((Integer) values.get(sizeHalf)).intValue()) / 2;
    }

    public static abstract interface ReduceClosure<Accumulator, Value>
    {

        public abstract Accumulator eval(Accumulator paramAccumulator, Value paramValue);
    }

    public static abstract interface MapClosure<From, To>
    {

        public abstract To eval(From paramFrom);
    }
}
