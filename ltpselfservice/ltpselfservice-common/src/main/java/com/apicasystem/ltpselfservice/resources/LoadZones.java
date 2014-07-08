/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice.resources;

import javax.json.JsonObject;

/**
 *
 * @author andras.nemes
 */
public enum LoadZones
{
  world(1, Countries.World, Providers.Aggregated),  ashburn(11, Countries.US, Providers.Amazon),  palo_alto(12, Countries.US, Providers.Amazon),  dublin(13, Countries.IE, Providers.Amazon),  singapore(14, Countries.SG, Providers.Amazon),  tokyo(15, Countries.JP, Providers.Amazon),  portland(22, Countries.US, Providers.Amazon),  san_paulo(23, Countries.BR, Providers.Amazon),  sydney(25, Countries.AU, Providers.Amazon),  chicago(26, Countries.US, Providers.Rackspace),  dallas(27, Countries.US, Providers.Rackspace),  london(28, Countries.UK, Providers.Rackspace),  sydney2(29, Countries.AU, Providers.Rackspace);
  
  public final int id;
  public final String uid;
  public final String city;
  public final Countries country;
  public final Providers provider;
  
  public static enum Countries
  {
    World,  US,  IE,  UK,  BR,  SG,  AU,  JP;
    
    private Countries() {}
  }
  
  public static enum Providers
  {
    Aggregated,  Amazon,  Rackspace;
    
    private Providers() {}
  }
  
  private LoadZones(int id, Countries country, Providers provider)
  {
    this.id = id;
    this.uid = String.format("%s:%s:%s", new Object[] { provider.name().toLowerCase(), country.name().toLowerCase(), name().toLowerCase() });
    this.city = toCityName(name());
    this.country = country;
    this.provider = provider;
  }
  
  @Override
  public String toString()
  {
    return this.uid;
  }
  
  public static LoadZones valueOf(JsonObject json)
  {
    String jsonId = json.getString("id", null);
    if (jsonId == null) {
      return world;
    }
    for (LoadZones z : values()) {
      if (z.uid.equals(jsonId)) {
        return z;
      }
    }
    return world;
  }
  
  public static LoadZones valueOf(int zoneId)
  {
    for (LoadZones z : values() ) {
      if (z.id == zoneId) {
        return z;
      }
    }
    return world;
  }
  
  private String toCityName(String s)
  {
    s = StringUtils.toInitialCase(s);
    int p = s.indexOf('_');
    if (p < 0) {
      return s;
    }
    StringBuilder buf = new StringBuilder(s);
    buf.setCharAt(p, ' ');
    p++;
    buf.setCharAt(p, Character.toUpperCase(buf.charAt(p)));
    return buf.toString();
  }
}
