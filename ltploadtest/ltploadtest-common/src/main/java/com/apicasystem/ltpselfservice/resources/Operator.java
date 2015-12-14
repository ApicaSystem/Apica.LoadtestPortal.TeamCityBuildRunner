package com.apicasystem.ltpselfservice.resources;

public enum Operator
{

    greaterThan(">", "&gt;", "greater than"), lessThan("<", "&lt;", "less than");

    public final String symbol;
    public final String label;
    public final String description;

    private Operator(String symbol, String label, String description)
    {
        this.symbol = symbol;
        this.label = label;
        this.description = description;
    }

    public String getId()
    {
        return name();
    }

    public String getLabel()
    {
        return this.label;
    }
    
    public String getDescription()
    {
        return this.description;
    }

    @Override
    public String toString()
    {
        return symbol;
    }
}
