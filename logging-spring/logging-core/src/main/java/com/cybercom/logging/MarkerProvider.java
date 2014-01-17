package com.cybercom.logging;


/**
 * The Interface MarkerProvider has method to get marker which is added to each 
 * log entry. Usual markers could be a username, session id etc.
 */
public interface MarkerProvider {
   
   String getMarker();

}
