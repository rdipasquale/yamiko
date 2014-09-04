package ar.edu.ungs.garules;

public class Constants {

	public static enum CENSUS_FIELDS{
		OC,SEX,AGE,HISPAN ,WHITE ,
		BLACK ,AIAN ,ASIAN ,
		NHPI ,OTHER , MARSTAT ,
		ENROLL ,
		GRADE ,EDUC ,
		SPEAK ,ENGABIL ,POB5 ,
		CITIZEN ,YR2US ,MOB ,
		MIGST5 ,SENSORY ,PHYSCL ,
		MENTAL ,SLFCARE ,ABGO ,
		ABWORK ,GRANDC ,
		RSPNSBL ,HOWLONG ,
		MILTARY ,VPS1 ,
		VPS2 ,
		VPS3 ,
		VPS4 ,
		VPS5 ,
		VPS6 ,
		VPS7 ,
		VPS8 ,
		VPS9,
		MILYRS ,VPSR ,
		ESR,ESP,
		POWST5 ,TRVMNS ,
		CARPOOL,LVTIME ,TRVTIME ,
		LAYOFF ,ABSENT ,RECALL ,
		LOOKWRK ,BACKWRK ,LASTWRK ,INDCEN ,OCCCEN5 ,
		CLWKR  ,WRKLYR ,WEEKS ,HOURS ,
		INCWS ,INCSE ,INCINT ,
		INCSS ,INCSSI ,
		INCPA ,INCRET ,
		INCOTH ,INCTOT ,
		EARNS ,POVERTY
	}
	
	public static final String[] CENSUS_FIELDS_DESCRIPTIONS=new String[]{
		"Own Child Indicator","Sex","Age","Hispanic or Latino Origin","White recode",
		"Black or African American","American Indian and Alaska Native","Asian",
		"Native Hawaiian and Other Pacific Islander","Some other race","Marital Status",
		"School Enrollment; Attended since February 1, 2000",
		"School Enrollment: Grade Level Attending","Educational Attainment",
		"Non-English Language","English Ability","Place of Birth",
		"Citizenship Status", "Year of Entry to United States", "Residence 5 Years Ago",
		"Migration State or Foreign Country Code", "Sensory Disability","Physical Disability",
		"Mental Disability", "Self-Care Disability", "Able to Go Out Disability",
		"Employment Disability", "Presence of Grandchildren under 18 years",
		"Responsible for Grandchildren","Length of Responsibility for Grandchildren",
		"Military Service", "Veteran’s Period of Service 1: On active duty April 1995 or later",
		"Veteran’s Period of Service 2: On active duty August 1990 to March 1995 (including Persian Gulf War)",
		"Veteran’s Period of Service 3: On active duty September 1980 to July 1990",
		"Veteran’s Period of Service 4: On active duty May 1975 to August 1980",
		"Veteran’s Period of Service 5: On active duty during the Vietnam Era (August 1964 to April 1975)",
		"Veteran’s Period of Service 6: On active duty February 1955 to July 1964",
		"Veteran’s Period of Service 7: On active duty during the Korean War (June 1950 to January 1955)",
		"Veteran’s Period of Service 8: On active duty during World War II (September 1940 to July 1947)",
		"Veteran’s Period of Service 9: On active duty any other time"	,
		"Years of Military Service", "Veteran’s Period of Service",
		"Employment Status Recode", "Employment Status of Parent(s)",
		"Place of Work State or Foreign Country Code","Means of Transportation to Work",
		"Vehicle Occupancy","Time Leaving for Work","Travel Time to Work",
		"Layoff from Job","Absent from Work","Return-to-Work Recall",
		"Looking for Work","Back to Work","Year Last Worked","Industry","Occupation",
		"Class of Worker", "Worked in 1999","Weeks Worked in 1999","Hours Per Week in 1999",
		"Wage/Salary Income in 1999","Self-Employment Income in 1999","Interest Income in 1999",
		"Social Security Income in 1999","Supplemental Security Income in 1999",
		"Public Assistance Income in 1999","Retirement Income in 1999",
		"Other Income in 1999","Person’s Total Income in 1999",
		"Person’s Total Earnings in 1999","Person’s Poverty Status"
	};

	public static final int[] CENSUS_FIELDS_POS_FROM=new int[]{
		20,23,25,28,32,33,34,35,36,37,44,49,51,53,64,70,72,76,78,83,85,119,121,123,125,127,129,132,134,136,138,140,141,142,143,144,145,146,147,148,150,
		152,154,156,157,191,194,196,200,204,205,206,207,208,209,211,223,234,236,238,241,244,251,258,265,271,277,283,290,297,305,312
	};
	
	public static final int[] CENSUS_FIELDS_LENGTH=new int[]{
		1,1,2,2,1,1,1,1,1,1,1,1,1,2,1,1,2,1,2,1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		2,1,1,2,2,1,2,3,1,1,1,1,1,1,3,3,1,1,2,2,6,6,6,5,5,5,6,6,7,7,3
	};
}
