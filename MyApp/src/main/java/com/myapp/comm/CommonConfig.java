package com.myapp.comm;


public class CommonConfig {
	
	private String serviceShutDownStartDt;
	
	private String serviceShutDownEndDt;
	
	/**
	 * @return the serviceShutDownStartDt
	 */
	public String getServiceShutDownStartDt() {
		return serviceShutDownStartDt;
	}

	/**
	 * @param serviceShutDownStartDt the serviceShutDownStartDt to set
	 */
	public void setServiceShutDownStartDt(String serviceShutDownStartDt) {
		this.serviceShutDownStartDt = serviceShutDownStartDt;
	}

	/**
	 * @return the serviceShutDownEndDt
	 */
	public String getServiceShutDownEndDt() {
		return serviceShutDownEndDt;
	}

	/**
	 * @param serviceShutDownEndDt the serviceShutDownEndDt to set
	 */
	public void setServiceShutDownEndDt(String serviceShutDownEndDt) {
		this.serviceShutDownEndDt = serviceShutDownEndDt;
	}
}
