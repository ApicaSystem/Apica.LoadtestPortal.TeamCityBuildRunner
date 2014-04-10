/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice;

/**
 *
 * @author andras.nemes
 */
public abstract interface LoadTestLogger
{
  public abstract void started(String paramString);  
  public abstract void finished(String paramString);  
  public abstract void message(String paramString);  
  public abstract void message(String paramString, Object... paramVarArgs);  
  public abstract void failure(String paramString);
}

