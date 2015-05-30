package com.myapp.comm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

@Service
public class FileService {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * @Desc	: JSON Map형식으로 File Write
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Utils
	 */
	public void writeFileToJSONMap (String sFile, Map mTmpData) {
		// Write JSON File
		try {
			mapper.writeValue(new File(sFile), mTmpData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Desc	: JSON List형식으로 File Write
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Utils
	 */
	public void writeFileToJSONList (String sFile, List rTmpData) {
		// Write JSON File
		try {
			mapper.writeValue(new File(sFile), rTmpData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Desc	: JSON Map형식 File Read
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Utils
	 */
	public Map readFileFromJSONMap (String sFile) {
		// Read JSON File
		Map mRtnData = new HashMap<String, Object>();
		
		try {
			mRtnData = mapper.readValue(new File(sFile), new TypeReference<HashMap<String,Object>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return mRtnData;
	}
	
	/**
	 * @Desc	: JSON List형식 File Read
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Utils
	 */
	public List readFileFromJSONList (String sFile) {
		// Read JSON File
		List rRtnData = new ArrayList();
		
		try {
			rRtnData = mapper.readValue(new File(sFile), new TypeReference<List>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rRtnData;
	}
}
