/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice.resources;

/**
 *
 * @author andras.nemes
 */
public enum Operator
{
    greaterThan(">", "&gt;"), lessThan("<", "&lt;");

    public final String symbol;
    public final String label;

    private Operator(String symbol, String label)
    {
        this.symbol = symbol;
        this.label = label;
    }

    public String getId()
    {
        return name();
    }

    public String getLabel()
    {
        return this.label;
    }
    
    @Override
    public String toString()
    {
        return symbol;
    }
}
