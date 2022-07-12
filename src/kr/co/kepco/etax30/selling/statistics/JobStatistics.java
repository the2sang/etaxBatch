package kr.co.kepco.etax30.selling.statistics;

public class JobStatistics {
    
    public static void main(String[] args) {
	int result = 0;
	
	StatisticsDao ssd = new StatisticsDao();
	result = ssd.tranMethodStatistics();
	System.out.println("매출 전송 방식별 통계정보>>> "+result+"건 생성");	
	result = ssd.classTypeStatistics();
	System.out.println("매출 종사업자 별 통계정보>>> "+result+"건 생성");
	result = ssd.systemTypeStatistics();
	System.out.println("매입 시스템 별 통계정보>>> "+result+"건 생성");
	result = ssd.buyingStatistics();
	System.out.println("매입처 별 통계정보>>> "+result+"건 생성");
	result = ssd.makeSaveTaxBillInfo();
	System.out.println("2개월전의 한달분 데이터 백업>>> "+result+">>테이블  생성");
	result = ssd.deleteTaxBillInfo();
	System.out.println("2개월전의 한달분 데이터 삭제>>> "+result+">>건 데이터  삭제");
    }

}
