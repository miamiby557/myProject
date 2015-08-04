package com.lnet.tmsapp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/7/10.
 */
public class DataUtils {

    public static List<DataItem> getTransportType(){
        List<DataItem> items = new ArrayList<>();
        DataItem item1 = new DataItem("1","公路零担");
        DataItem item2 = new DataItem("2","公路快线");
        DataItem item3 = new DataItem("3","快递");
        DataItem item4 = new DataItem("4","空运晚班");
        DataItem item5 = new DataItem("5","空运早班");
        DataItem item6 = new DataItem("6","公路整车");
        DataItem item7 = new DataItem("7","海运");
        DataItem item8 = new DataItem("8","铁路");
        DataItem item9 = new DataItem("9","内河航运");
        DataItem defaultItem = new DataItem("","请选择运输方式");
        items.add(defaultItem);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        items.add(item7);
        items.add(item8);
        items.add(item9);
        return items;
    }



    public static List<DataItem> getCalculateType(){
        List<DataItem> items = new ArrayList<>();
        DataItem item3 = new DataItem("3","体积");
        DataItem item1 = new DataItem("1","重量");
        DataItem item2 = new DataItem("2","件数");
        DataItem defaultItem = new DataItem("","请选择计费方式");
        items.add(defaultItem);
        items.add(item3);
        items.add(item2);
        items.add(item1);
        return items;
    }

    public static List<DataItem> getPaymentType(){
        List<DataItem> items = new ArrayList<>();
        DataItem item3 = new DataItem("3","转账");
        DataItem item1 = new DataItem("1","现金");
        DataItem item2 = new DataItem("2","支票");
        DataItem item4 = new DataItem("4","油卡付");
        DataItem item5 = new DataItem("5","油卡付+现金");
        DataItem defaultItem = new DataItem("","请选择计费方式");
        items.add(defaultItem);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        return items;
    }

}
