package com.apicasystem.ltpselfservice.resources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoundedDroppingQueue<ElementType>
        implements Iterable<ElementType>
{

    private static int defaultSize = 1;
    private final Object[] elements;
    private int putIdx = 0;
    private int getIdx = 0;
    private int size = 0;

    public BoundedDroppingQueue(int size)
    {
        this.elements = new Object[size];
    }

    public BoundedDroppingQueue()
    {
        this(defaultSize);
    }

    public static void setDefaultSize(int defaultSize)
    {
        defaultSize = defaultSize;
    }

    public void put(ElementType x)
    {
        if (full())
        {
            this.getIdx = ((this.getIdx + 1) % this.elements.length);
        } else
        {
            this.size += 1;
        }
        this.elements[this.putIdx] = x;
        this.putIdx = ((this.putIdx + 1) % this.elements.length);
    }

    public ElementType get()
    {
        if (empty())
        {
            throw new IllegalArgumentException("Empty queue");
        }
        ElementType x = (ElementType) this.elements[this.getIdx];
        this.getIdx = ((this.getIdx + 1) % this.elements.length);
        this.size -= 1;
        return x;
    }

    public int size()
    {
        return this.size;
    }

    public boolean empty()
    {
        return size() == 0;
    }

    public boolean full()
    {
        return size() == this.elements.length;
    }

    public Iterator<ElementType> iterator()
    {
        return new Iterator()
        {
            int idx = BoundedDroppingQueue.this.getIdx;
            int N = BoundedDroppingQueue.this.size;

            public boolean hasNext()
            {
                return this.N > 0;
            }

            public ElementType next()
            {
                ElementType x = (ElementType) BoundedDroppingQueue.this.elements[this.idx];
                this.idx = ((this.idx + 1) % BoundedDroppingQueue.this.elements.length);
                this.N -= 1;
                return x;
            }

            public void remove()
            {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    public List<ElementType> toList()
    {
        List<ElementType> result = new ArrayList(size());
        ElementType e;
        for (Iterator i = iterator(); i.hasNext(); result.add(e))
        {
            e = (ElementType) i.next();
        }
        return result;
    }

    @Override
    public String toString()
    {
        return toList().toString();
    }
}
