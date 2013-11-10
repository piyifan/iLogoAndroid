package com.iLogo.error;

import com.iLogo.driver.Location;


/**
 *   iLogo中所有编译错误的基类
 */
public abstract class BaseError {

		/**
		 * 编译错误所在的位置
		 */
		protected Location location;

		/**
		 * @return 返回错误的具体描述
		 */
		protected abstract String getErrMsg();

		public BaseError(Location location) {
			this.location = location;
		}

		public Location getLocation() {
			return location;
		}

		/**
		 * 返回包含位置信息在内的完整错误信息
		 */
		@Override
		public String toString() {
			if (location.equals(Location.NO_LOCATION)) {
				return "*** Error: " + getErrMsg();
			} else {
				return "*** Error at " + location + ": " + getErrMsg();
			}
		}
}
