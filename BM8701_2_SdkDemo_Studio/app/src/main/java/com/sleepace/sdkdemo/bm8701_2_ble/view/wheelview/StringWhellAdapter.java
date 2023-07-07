package com.sleepace.sdkdemo.bm8701_2_ble.view.wheelview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringWhellAdapter implements WheelAdapter{

    private List<String> list = null;

    public StringWhellAdapter(List<String> list){
        this.list = list;
    }
    public StringWhellAdapter(Collection<String> collection){
        for(String s:collection){
            list.add(s);
        }
    }

    @Override
    public int getItemsCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public String getItem(int index) {
        if(getItemsCount() > 0){
            return list.get(index);
        }
        return "";
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
}
