package com.github.lzenczuk.ngs;

/**
 * @author lzenczuk 13/08/2015
 */
public class EngineManager {

    private Engine engine;

    public Engine getEngine(){
        if(engine==null){
            engine = new Engine();
        }

        return engine;
    }
}
