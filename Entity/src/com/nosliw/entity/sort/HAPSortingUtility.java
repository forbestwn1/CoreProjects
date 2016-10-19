package com.nosliw.entity.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data1.HAPWraper;

public class HAPSortingUtility {

	/*
	 * sort a list according to sorting info list
	 */
	public static void sort(List<HAPWraper> datas, final List<HAPSortingInfo> sortingInfos){
		Collections.sort(datas, new HAPSortingComparator(sortingInfos));
	}

	/*
	 * find position of data within sorted datas according to sortingInfos
	 */
	public static int getPosition(HAPWraper data, List<HAPWraper> datas, List<HAPSortingInfo> sortingInfos){
		int position = Collections.binarySearch(datas, data, new HAPSortingComparator(sortingInfos));
		return position;
	}

}

class HAPSortingComparator implements Comparator<HAPWraper>{
	final private List<HAPSortingInfo> m_sortingInfos;
	
	public HAPSortingComparator(List<HAPSortingInfo> sortingInfos){
		this.m_sortingInfos = sortingInfos;
	}
	
	@Override
	public int compare(HAPWraper o1, HAPWraper o2) {
		int out = HAPConstant.COMPARE_EQUAL;
		for(HAPSortingInfo sortingInfo : this.m_sortingInfos){
			out = sortingInfo.compare(o1, o2);
			if(out!=HAPConstant.COMPARE_EQUAL)		break;
		}
		return out;
	}
}
