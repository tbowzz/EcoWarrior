package com.ragnardev.ecowarrior.OBD.io;

public interface ObdProgressListener {

    void stateUpdate(final ObdCommandJob job);

}