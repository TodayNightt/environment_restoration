package com.game.Utils;

import com.game.Terrain.Chunk;

import java.util.List;

public class WorkerManager {

    private static WorkerManager instance;
    private List<Chunk> chunkList;
    private int totalNumber,currentNum;
    public static boolean stillWorking;

    public static WorkerManager getInstance(){
        if(instance == null){
            instance = new WorkerManager();
        }
        return instance;
    }

    public void run(){
        stillWorking = totalNumber != currentNum;
        List<Chunk> stream = chunkList.stream().filter(chunk -> !chunk.isAlive() && chunk.getMesh() == null).toList();
        currentNum += stream.size();
        stream.forEach(Chunk::generateMesh);
    }

    public void start(){
        chunkList.forEach(Thread::start);
    }

    public WorkerManager pass(List<Chunk> chunkList){
        this.chunkList = chunkList;
        this.totalNumber = chunkList.size();
        this.currentNum = 0;
        stillWorking = true;
        return this;
    }

}
