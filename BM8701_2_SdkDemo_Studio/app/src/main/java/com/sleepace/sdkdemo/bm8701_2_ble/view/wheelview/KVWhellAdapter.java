package com.sleepace.sdkdemo.bm8701_2_ble.view.wheelview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KVWhellAdapter implements WheelAdapter{

    private List<String> key = new ArrayList<>();
    private List<Integer> value = new ArrayList<>();

    public KVWhellAdapter(List<String> key,List<Integer> value){
        this.key = key;
        this.value = value;

    }
    public KVWhellAdapter(Collection<String> keyC,Collection<Integer> valueC){
       for(String s:keyC){
           key.add(s);
       }
        for(int i:valueC){
            value.add(i);
        }
    }
    @Override
    public int getItemsCount() {
        return key.size();
    }

    @Override
    public String getItem(int index) {
        return key.get(index);
    }

    @Override
    public int indexOf(Object o) {

        int index = 0;
        int count = getItemsCount();
        if(count > 0){
            for(int i=0; i<count; i++){
                if(getItem(i).equals(o)){
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public int getValue(int index){
        return value.get(index);
    }
}
