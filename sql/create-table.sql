create table BTMP_TAX_BILL_INFO
(
    ROW_NO              NUMBER,
    MACHINE_IDX         NUMBER,
    THREAD_IDX          NUMBER,
    ROW_ID              ROWID,
    CHARGE_TOTAL_AMOUNT NUMBER(18) not null,
    TAX_TOTAL_AMOUNT    NUMBER(18) not null,
    FLAG                VARCHAR2(1) default 'N'
)
    /

comment on table BTMP_TAX_BILL_INFO is '세금계산서 쓰레드정보'
/

comment on column BTMP_TAX_BILL_INFO.ROW_NO is '번호'
/

comment on column BTMP_TAX_BILL_INFO.MACHINE_IDX is '서버번호'
/

comment on column BTMP_TAX_BILL_INFO.THREAD_IDX is '쓰레드 수'
/

comment on column BTMP_TAX_BILL_INFO.ROW_ID is '로아이디'
/

comment on column BTMP_TAX_BILL_INFO.CHARGE_TOTAL_AMOUNT is '총 공급가액 합계'
/

comment on column BTMP_TAX_BILL_INFO.TAX_TOTAL_AMOUNT is '총 세액 합계'
/

comment on column BTMP_TAX_BILL_INFO.FLAG is 'XML 생성 성공여부'
/

create index BTMP_TAX_BILL_INFO_IX1
    on BTMP_TAX_BILL_INFO (MACHINE_IDX, THREAD_IDX)
    /

create index BTMP_TAX_BILL_INFO_IX2
    on BTMP_TAX_BILL_INFO (ROW_ID)
    /

create table IF_TAX_BILL_CONFIRM_INFO
(
    REL_SYSTEM_ID VARCHAR2(10)         not null,
    JOB_GUB_CODE  VARCHAR2(6)          not null,
    MANAGE_ID     VARCHAR2(24)         not null,
    STATUS_CODE   VARCHAR2(2)          not null,
    STATUS_DESC   VARCHAR2(100),
    REGIST_DT     DATE default SYSDATE not null,
    ISSUE_ID      VARCHAR2(24)         not null,
    EAI_STAT      CHAR,
    EAI_CDATE     VARCHAR2(14),
    EAI_UDATE     VARCHAR2(14)
)
    /

comment on table IF_TAX_BILL_CONFIRM_INFO is '매출세금계산서 공급자 확정 연계 정보'
/

comment on column IF_TAX_BILL_CONFIRM_INFO.REL_SYSTEM_ID is '연계시스템ID'
/

comment on column IF_TAX_BILL_CONFIRM_INFO.JOB_GUB_CODE is '업무구분코드(전력매출,공사매출 등)'
/

comment on column IF_TAX_BILL_CONFIRM_INFO.MANAGE_ID is '사업자관리번호'
/

comment on column IF_TAX_BILL_CONFIRM_INFO.STATUS_CODE is '처리상태코드'
/

comment on column IF_TAX_BILL_CONFIRM_INFO.STATUS_DESC is '처리상태설명:오류발생시 오류내용을 기재함'
/

comment on column IF_TAX_BILL_CONFIRM_INFO.REGIST_DT is '등록일시'
/

comment on column IF_TAX_BILL_CONFIRM_INFO.ISSUE_ID is '승인번호'
/

comment on column IF_TAX_BILL_CONFIRM_INFO.EAI_STAT is 'EAI송수신상태'
/

comment on column IF_TAX_BILL_CONFIRM_INFO.EAI_CDATE is 'EAI입력일자'
/

comment on column IF_TAX_BILL_CONFIRM_INFO.EAI_UDATE is 'EAI변경일자'
/

create unique index IF_TAX_BILL_CONFIRM_INFO_PK
    on IF_TAX_BILL_CONFIRM_INFO (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID, STATUS_CODE, REGIST_DT)
    /

alter table IF_TAX_BILL_CONFIRM_INFO
    add primary key (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID, STATUS_CODE, REGIST_DT)
    /

create table IF_TAX_BILL_INFO_TEMP
(
    MANAGE_ID                   VARCHAR2(24)         not null,
    ISSUE_DAY                   VARCHAR2(8)          not null,
    BILL_TYPE_CODE              VARCHAR2(4)          not null,
    PURPOSE_CODE                VARCHAR2(2),
    AMENDMENT_CODE              VARCHAR2(2),
    DESCRIPTION                 VARCHAR2(150),
    UPPER_MANAGE_ID             VARCHAR2(24),
    INVOICER_PARTY_ID           VARCHAR2(13)         not null,
    INVOICER_TAX_REGIST_ID      VARCHAR2(4),
    INVOICER_PARTY_NAME         VARCHAR2(70)         not null,
    INVOICER_CEO_NAME           VARCHAR2(30)         not null,
    INVOICER_ADDR               VARCHAR2(150),
    INVOICER_TYPE               VARCHAR2(40),
    INVOICER_CLASS              VARCHAR2(40),
    INVOICER_CONTACT_DEPART     VARCHAR2(40),
    INVOICER_CONTACT_NAME       VARCHAR2(30),
    INVOICER_CONTACT_PHONE      VARCHAR2(20),
    INVOICER_CONTACT_EMAIL      VARCHAR2(40),
    INVOICEE_BUSINESS_TYPE_CODE VARCHAR2(2)          not null,
    INVOICEE_PARTY_ID           VARCHAR2(13)         not null,
    INVOICEE_TAX_REGIST_ID      VARCHAR2(4),
    INVOICEE_PARTY_NAME         VARCHAR2(70)         not null,
    INVOICEE_CEO_NAME           VARCHAR2(30)         not null,
    INVOICEE_ADDR               VARCHAR2(150)        not null,
    INVOICEE_TYPE               VARCHAR2(40),
    INVOICEE_CLASS              VARCHAR2(40),
    INVOICEE_CONTACT_DEPART1    VARCHAR2(40),
    INVOICEE_CONTACT_NAME1      VARCHAR2(30),
    INVOICEE_CONTACT_PHONE1     VARCHAR2(20),
    INVOICEE_CONTACT_EMAIL1     VARCHAR2(40),
    INVOICEE_CONTACT_DEPART2    VARCHAR2(40),
    INVOICEE_CONTACT_NAME2      VARCHAR2(30),
    INVOICEE_CONTACT_PHONE2     VARCHAR2(20),
    INVOICEE_CONTACT_EMAIL2     VARCHAR2(40),
    PAYMENT_TYPE_CODE1          VARCHAR2(2),
    PAY_AMOUNT1                 NUMBER(18),
    PAYMENT_TYPE_CODE2          VARCHAR2(2),
    PAY_AMOUNT2                 NUMBER(18),
    PAYMENT_TYPE_CODE3          VARCHAR2(2),
    PAY_AMOUNT3                 NUMBER(18),
    PAYMENT_TYPE_CODE4          VARCHAR2(2),
    PAY_AMOUNT4                 NUMBER(18),
    CHARGE_TOTAL_AMOUNT         NUMBER(18)           not null,
    TAX_TOTAL_AMOUNT            NUMBER(18)           not null,
    GRAND_TOTAL_AMOUNT          NUMBER(18)           not null,
    JOB_GUB_CODE                VARCHAR2(6)          not null,
    REL_SYSTEM_ID               VARCHAR2(10)         not null,
    REGIST_DT                   DATE default SYSDATE not null,
    MODIFY_DT                   DATE default SYSDATE not null,
    CANCEL_DT                   DATE,
    ISSUE_ID                    VARCHAR2(24)         not null,
    ADD_TAX_YN                  CHAR,
    BILL_ISSUE_YYYYMM           VARCHAR2(6),
    ONLINE_GUB_CODE             VARCHAR2(1)
)
    /

comment on table IF_TAX_BILL_INFO_TEMP is '세금계산서정보 인터페이스'
/

comment on column IF_TAX_BILL_INFO_TEMP.MANAGE_ID is '사업자관리번호'
/

comment on column IF_TAX_BILL_INFO_TEMP.ISSUE_DAY is '작성일자(YYYYMMDD형식): 매출기준일자'
/

comment on column IF_TAX_BILL_INFO_TEMP.BILL_TYPE_CODE is '세금계산서종류'
/

comment on column IF_TAX_BILL_INFO_TEMP.PURPOSE_CODE is '영수청구코드'
/

comment on column IF_TAX_BILL_INFO_TEMP.AMENDMENT_CODE is '전자세금계산서 수정 사유코드'
/

comment on column IF_TAX_BILL_INFO_TEMP.DESCRIPTION is '비고'
/

comment on column IF_TAX_BILL_INFO_TEMP.UPPER_MANAGE_ID is '부모관리번호 : 수정세금계산서 발행시 사용'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICER_PARTY_ID is '공급자 사업자등록번호'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICER_TAX_REGIST_ID is '종사업장 식별코드'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICER_PARTY_NAME is '공급업체 사업체명'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICER_CEO_NAME is '공급업체 대표자명'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICER_ADDR is '공급업체의 주소'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICER_TYPE is '공급업체의 업태'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICER_CLASS is '공급업체의 업종'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICER_CONTACT_DEPART is '공급업체 담당부서'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICER_CONTACT_NAME is '공급업체 담당자명'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICER_CONTACT_PHONE is '공급업체 담당자 전화번호'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICER_CONTACT_EMAIL is '공급업체 담당자 이메일'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_BUSINESS_TYPE_CODE is '공급받는자 사업자등록번호 구분코드'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_PARTY_ID is '공급받는자 사업자등록번호'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_TAX_REGIST_ID is '공급받는자 종사업장 식별코드'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_PARTY_NAME is '공급받는자 사업체명'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_CEO_NAME is '공급받는자 대표자명'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_ADDR is '공급받는자 주소'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_TYPE is '공급받는자 업태'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_CLASS is '공급받는자 업종'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_CONTACT_DEPART1 is '공급받는자 담당부서'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_CONTACT_NAME1 is '공급받는자 담당자명'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_CONTACT_PHONE1 is '공급받는자 담당자 전화번호'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_CONTACT_EMAIL1 is '공급받는자 담당자 이메일'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_CONTACT_DEPART2 is '공급받는자 담당부서'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_CONTACT_NAME2 is '공급받는자 담당자명'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_CONTACT_PHONE2 is '공급받는자 담당자 전화번호'
/

comment on column IF_TAX_BILL_INFO_TEMP.INVOICEE_CONTACT_EMAIL2 is '공급받는자 담당자 이메일'
/

comment on column IF_TAX_BILL_INFO_TEMP.PAYMENT_TYPE_CODE1 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO_TEMP.PAY_AMOUNT1 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO_TEMP.PAYMENT_TYPE_CODE2 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO_TEMP.PAY_AMOUNT2 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO_TEMP.PAYMENT_TYPE_CODE3 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO_TEMP.PAY_AMOUNT3 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO_TEMP.PAYMENT_TYPE_CODE4 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO_TEMP.PAY_AMOUNT4 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO_TEMP.CHARGE_TOTAL_AMOUNT is '총 공급가액 합계'
/

comment on column IF_TAX_BILL_INFO_TEMP.TAX_TOTAL_AMOUNT is '총 세액 합계'
/

comment on column IF_TAX_BILL_INFO_TEMP.GRAND_TOTAL_AMOUNT is '총액(공급가액+세액)'
/

comment on column IF_TAX_BILL_INFO_TEMP.JOB_GUB_CODE is '업무구분코드(전력매출,공사매출 등)'
/

comment on column IF_TAX_BILL_INFO_TEMP.REL_SYSTEM_ID is '연계시스템ID'
/

comment on column IF_TAX_BILL_INFO_TEMP.REGIST_DT is '등록일시'
/

comment on column IF_TAX_BILL_INFO_TEMP.MODIFY_DT is '수정일시'
/

comment on column IF_TAX_BILL_INFO_TEMP.CANCEL_DT is '취소일시'
/

comment on column IF_TAX_BILL_INFO_TEMP.ISSUE_ID is '승인번호'
/

comment on column IF_TAX_BILL_INFO_TEMP.ADD_TAX_YN is '가산세 여부'
/

comment on column IF_TAX_BILL_INFO_TEMP.BILL_ISSUE_YYYYMM is '청구서발행년월'
/

comment on column IF_TAX_BILL_INFO_TEMP.ONLINE_GUB_CODE is '온,오프라인 구분코드(1:온라인,2:오프라인)'
/

create index IF_TAX_BILL_INFO_TEMP_IX1
    on IF_TAX_BILL_INFO_TEMP (ONLINE_GUB_CODE, MODIFY_DT, ISSUE_DAY)
    /

create unique index IF_TAX_BILL_INFO_TEMP_PK
    on IF_TAX_BILL_INFO_TEMP (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID)
    /

alter table IF_TAX_BILL_INFO_TEMP
    add primary key (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID)
    /

create table IF_TAX_BILL_RESULT_INFO
(
    REL_SYSTEM_ID VARCHAR2(10)         not null,
    JOB_GUB_CODE  VARCHAR2(6)          not null,
    MANAGE_ID     VARCHAR2(24)         not null,
    STATUS_CODE   VARCHAR2(2)          not null,
    STATUS_DESC   VARCHAR2(100),
    REGIST_DT     DATE default SYSDATE not null,
    MODIFY_DT     DATE default SYSDATE not null,
    ISSUE_ID      VARCHAR2(24)         not null,
    EAI_STAT      CHAR,
    EAI_CDATE     VARCHAR2(14),
    EAI_UDATE     VARCHAR2(14)
)
    /

comment on table IF_TAX_BILL_RESULT_INFO is '업무시스템연계 매출세금계산서 처리결과 정보'
/

comment on column IF_TAX_BILL_RESULT_INFO.REL_SYSTEM_ID is '연계시스템ID'
/

comment on column IF_TAX_BILL_RESULT_INFO.JOB_GUB_CODE is '업무구분코드(전력매출,공사매출 등)'
/

comment on column IF_TAX_BILL_RESULT_INFO.MANAGE_ID is '사업자관리번호'
/

comment on column IF_TAX_BILL_RESULT_INFO.STATUS_CODE is '처리상태코드'
/

comment on column IF_TAX_BILL_RESULT_INFO.STATUS_DESC is '처리상태설명:오류발생시 오류내용을 기재함'
/

comment on column IF_TAX_BILL_RESULT_INFO.REGIST_DT is '등록일시'
/

comment on column IF_TAX_BILL_RESULT_INFO.MODIFY_DT is '수정일시'
/

comment on column IF_TAX_BILL_RESULT_INFO.ISSUE_ID is '승인번호'
/

comment on column IF_TAX_BILL_RESULT_INFO.EAI_STAT is 'EAI송수신상태'
/

comment on column IF_TAX_BILL_RESULT_INFO.EAI_CDATE is 'EAI입력일자'
/

comment on column IF_TAX_BILL_RESULT_INFO.EAI_UDATE is 'EAI변경일자'
/

create unique index IF_TAX_BILL_RESULT_INFO_PK
    on IF_TAX_BILL_RESULT_INFO (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID, STATUS_CODE, REGIST_DT)
    /

alter table IF_TAX_BILL_RESULT_INFO
    add primary key (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID, STATUS_CODE, REGIST_DT)
    /

create table TB_STATUS_HIST
(
    IO_CODE       VARCHAR2(1)  not null,
    ISSUE_DAY     VARCHAR2(8)  not null,
    BIZ_MANAGE_ID VARCHAR2(24) not null,
    SEQ_NO        NUMBER(2)    not null,
    AVL_END_DT    VARCHAR2(14) not null,
    AVL_BEGIN_DT  VARCHAR2(14) not null,
    STATUS_CODE   VARCHAR2(2),
    REGIST_DT     DATE,
    REGIST_ID     VARCHAR2(20),
    STATUS_DESC   VARCHAR2(100)
)
    /

comment on table TB_STATUS_HIST is '상태이력'
/

comment on column TB_STATUS_HIST.IO_CODE is '매입매출코드 1:매출, 2:매입'
/

comment on column TB_STATUS_HIST.ISSUE_DAY is '작성일자'
/

comment on column TB_STATUS_HIST.BIZ_MANAGE_ID is '사업자관리번호'
/

comment on column TB_STATUS_HIST.SEQ_NO is '물품 일련번호'
/

comment on column TB_STATUS_HIST.AVL_END_DT is '유효종료일시'
/

comment on column TB_STATUS_HIST.AVL_BEGIN_DT is '유효시작일시'
/

comment on column TB_STATUS_HIST.STATUS_CODE is '진행상태'
/

comment on column TB_STATUS_HIST.REGIST_DT is '등록일시'
/

comment on column TB_STATUS_HIST.REGIST_ID is '등록자ID'
/

comment on column TB_STATUS_HIST.STATUS_DESC is '처리상태설명:오류발생시 오류내용을 기재함'
/

create unique index TB_STATUS_HIST_PK
    on TB_STATUS_HIST (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, SEQ_NO)
    /

create table TB_XML_INFO
(
    IO_CODE       VARCHAR2(1)  not null,
    ISSUE_DAY     VARCHAR2(8)  not null,
    BIZ_MANAGE_ID VARCHAR2(24) not null,
    ORG_XML_DOC   BLOB,
    R_VALUE       VARCHAR2(100),
    REGIST_DT     DATE,
    MODIFY_DT     DATE,
    REGIST_ID     VARCHAR2(20),
    MODIFY_ID     VARCHAR2(20),
    constraint TB_XML_INFO_PK
        primary key (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID)
)
    /

comment on table TB_XML_INFO is '매출세금계산서 XML문서 정보'
/

comment on column TB_XML_INFO.IO_CODE is '매입매출코드 1:매출, 2:매입'
/

comment on column TB_XML_INFO.ISSUE_DAY is '작성일'
/

comment on column TB_XML_INFO.BIZ_MANAGE_ID is '사업자관리번호'
/

comment on column TB_XML_INFO.ORG_XML_DOC is 'XML문서'
/

comment on column TB_XML_INFO.R_VALUE is 'R_값'
/

comment on column TB_XML_INFO.REGIST_DT is '등록일시'
/

comment on column TB_XML_INFO.MODIFY_DT is '수정일시'
/

comment on column TB_XML_INFO.REGIST_ID is '등록자ID'
/

comment on column TB_XML_INFO.MODIFY_ID is '수정자ID'
/

create table TB_TRADE_ITEM_LIST_BEF
(
    IO_CODE        VARCHAR2(1)  not null,
    ISSUE_DAY      VARCHAR2(8)  not null,
    BIZ_MANAGE_ID  VARCHAR2(24) not null,
    SEQ_NO         NUMBER(2)    not null,
    PURCHASE_DAY   VARCHAR2(8),
    ITEM_NAME      VARCHAR2(100),
    ITEM_INFO      VARCHAR2(60),
    ITEM_DESC      VARCHAR2(100),
    UNIT_QUANTITY  NUMBER(10, 2),
    UNIT_AMOUNT    NUMBER(16, 2),
    INVOICE_AMOUNT NUMBER(18),
    TAX_AMOUNT     NUMBER(18)
)
    /

comment on table TB_TRADE_ITEM_LIST_BEF is '아이탬백업정보'
/

comment on column TB_TRADE_ITEM_LIST_BEF.IO_CODE is '매입매출코드 1:매출, 2:매입'
/

comment on column TB_TRADE_ITEM_LIST_BEF.ISSUE_DAY is '작성일자'
/

comment on column TB_TRADE_ITEM_LIST_BEF.BIZ_MANAGE_ID is '사업자관리번호'
/

comment on column TB_TRADE_ITEM_LIST_BEF.SEQ_NO is '물품 일련번호'
/

comment on column TB_TRADE_ITEM_LIST_BEF.PURCHASE_DAY is '물품 공급일자'
/

comment on column TB_TRADE_ITEM_LIST_BEF.ITEM_NAME is '물품명'
/

comment on column TB_TRADE_ITEM_LIST_BEF.ITEM_INFO is '물품에 대한 규격'
/

comment on column TB_TRADE_ITEM_LIST_BEF.ITEM_DESC is '물품과 관련된 자유기술문'
/

comment on column TB_TRADE_ITEM_LIST_BEF.UNIT_QUANTITY is '수량'
/

comment on column TB_TRADE_ITEM_LIST_BEF.UNIT_AMOUNT is '물품 단가'
/

comment on column TB_TRADE_ITEM_LIST_BEF.INVOICE_AMOUNT is '물품 공급 가액'
/

comment on column TB_TRADE_ITEM_LIST_BEF.TAX_AMOUNT is '물품 세액'
/

create index TB_TRADE_ITEM_LIST_BEF_PK
    on TB_TRADE_ITEM_LIST_BEF (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, SEQ_NO)
    /

create table TB_XML_INFO_BEF
(
    IO_CODE       VARCHAR2(1)  not null,
    ISSUE_DAY     VARCHAR2(8)  not null,
    BIZ_MANAGE_ID VARCHAR2(24) not null,
    ORG_XML_DOC   BLOB,
    R_VALUE       VARCHAR2(100),
    REGIST_DT     DATE,
    MODIFY_DT     DATE,
    REGIST_ID     VARCHAR2(20),
    MODIFY_ID     VARCHAR2(20)
)
    /

comment on table TB_XML_INFO_BEF is '전자세금계산서 XML 백업정보'
/

comment on column TB_XML_INFO_BEF.IO_CODE is '매입매출코드 1:매출, 2:매입'
/

comment on column TB_XML_INFO_BEF.ISSUE_DAY is '작성일'
/

comment on column TB_XML_INFO_BEF.BIZ_MANAGE_ID is '사업자관리번호'
/

comment on column TB_XML_INFO_BEF.ORG_XML_DOC is 'XML문서'
/

comment on column TB_XML_INFO_BEF.R_VALUE is 'R_값'
/

comment on column TB_XML_INFO_BEF.REGIST_DT is '등록일시'
/

comment on column TB_XML_INFO_BEF.MODIFY_DT is '수정일시'
/

comment on column TB_XML_INFO_BEF.REGIST_ID is '등록자ID'
/

comment on column TB_XML_INFO_BEF.MODIFY_ID is '수정자ID'
/

create index TB_XML_INFO_BEF_IX
    on TB_XML_INFO_BEF (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID)
    /

create table TB_TAX_BILL_INFO_BEF
(
    IO_CODE                     VARCHAR2(1)                 not null,
    ISSUE_DAY                   VARCHAR2(8)                 not null,
    BIZ_MANAGE_ID               VARCHAR2(24)                not null,
    SVC_MANAGE_ID               VARCHAR2(24),
    ISSUE_DT                    VARCHAR2(14),
    SIGNATURE                   VARCHAR2(1024),
    ISSUE_ID                    VARCHAR2(24)                not null,
    BILL_TYPE_CODE              VARCHAR2(4)                 not null,
    PURPOSE_CODE                VARCHAR2(2),
    AMENDMENT_CODE              VARCHAR2(2),
    DESCRIPTION                 VARCHAR2(150),
    IMPORT_DOC_ID               VARCHAR2(15),
    IMPORT_PERIOD_START_DAY     VARCHAR2(8),
    IMPORT_PERIOD_END_DAY       VARCHAR2(8),
    IMPORT_ITEM_QUANTITY        NUMBER(6),
    INVOICER_PARTY_ID           VARCHAR2(13)                not null,
    INVOICER_TAX_REGIST_ID      VARCHAR2(4),
    INVOICER_PARTY_NAME         VARCHAR2(70)                not null,
    INVOICER_CEO_NAME           VARCHAR2(30)                not null,
    INVOICER_ADDR               VARCHAR2(150),
    INVOICER_TYPE               VARCHAR2(40),
    INVOICER_CLASS              VARCHAR2(40),
    INVOICER_CONTACT_DEPART     VARCHAR2(40),
    INVOICER_CONTACT_NAME       VARCHAR2(30),
    INVOICER_CONTACT_PHONE      VARCHAR2(20),
    INVOICER_CONTACT_EMAIL      VARCHAR2(40),
    INVOICEE_BUSINESS_TYPE_CODE VARCHAR2(2)                 not null,
    INVOICEE_PARTY_ID           VARCHAR2(13)                not null,
    INVOICEE_TAX_REGIST_ID      VARCHAR2(4),
    INVOICEE_PARTY_NAME         VARCHAR2(70)                not null,
    INVOICEE_CEO_NAME           VARCHAR2(30)                not null,
    INVOICEE_ADDR               VARCHAR2(150),
    INVOICEE_TYPE               VARCHAR2(40),
    INVOICEE_CLASS              VARCHAR2(40),
    INVOICEE_CONTACT_DEPART1    VARCHAR2(40),
    INVOICEE_CONTACT_NAME1      VARCHAR2(30),
    INVOICEE_CONTACT_PHONE1     VARCHAR2(20),
    INVOICEE_CONTACT_EMAIL1     VARCHAR2(40),
    INVOICEE_CONTACT_DEPART2    VARCHAR2(40),
    INVOICEE_CONTACT_NAME2      VARCHAR2(30),
    INVOICEE_CONTACT_PHONE2     VARCHAR2(20),
    INVOICEE_CONTACT_EMAIL2     VARCHAR2(40),
    BROKER_PARTY_ID             VARCHAR2(13),
    BROKER_TAX_REGIST_ID        VARCHAR2(4),
    BROKER_PARTY_NAME           VARCHAR2(70),
    BROKER_CEO_NAME             VARCHAR2(30),
    BROKER_ADDR                 VARCHAR2(150),
    BROKER_TYPE                 VARCHAR2(40),
    BROKER_CLASS                VARCHAR2(40),
    BROKER_CONTACT_DEPART       VARCHAR2(40),
    BROKER_CONTACT_NAME         VARCHAR2(30),
    BROKER_CONTACT_PHONE        VARCHAR2(20),
    BROKER_CONTACT_EMAIL        VARCHAR2(40),
    PAYMENT_TYPE_CODE1          VARCHAR2(2),
    PAY_AMOUNT1                 NUMBER(18),
    PAYMENT_TYPE_CODE2          VARCHAR2(2),
    PAY_AMOUNT2                 NUMBER(18),
    PAYMENT_TYPE_CODE3          VARCHAR2(2),
    PAY_AMOUNT3                 NUMBER(18),
    PAYMENT_TYPE_CODE4          VARCHAR2(2),
    PAY_AMOUNT4                 NUMBER(18),
    CHARGE_TOTAL_AMOUNT         NUMBER(18)                  not null,
    TAX_TOTAL_AMOUNT            NUMBER(18)                  not null,
    GRAND_TOTAL_AMOUNT          NUMBER(18)                  not null,
    STATUS_CODE                 VARCHAR2(2),
    ELECTRONIC_REPORT_YN        VARCHAR2(1) default 'N'     not null,
    ONLINE_GUB_CODE             VARCHAR2(1),
    ADD_TAX_YN                  VARCHAR2(1) default 'N'     not null,
    INVOICEE_GUB_CODE           VARCHAR2(2),
    REL_SYSTEM_ID               VARCHAR2(10),
    JOB_GUB_CODE                VARCHAR2(6),
    REGIST_DT                   DATE        default SYSDATE not null,
    MODIFY_DT                   DATE        default SYSDATE not null,
    REGIST_ID                   VARCHAR2(20),
    MODIFY_ID                   VARCHAR2(30),
    UPPER_MANAGE_ID             VARCHAR2(24),
    ERP_SND_YN                  CHAR
)
    /

comment on table TB_TAX_BILL_INFO_BEF is '매 출 세 금 계 산 서  기 본 백 업 정 보'
/

comment on column TB_TAX_BILL_INFO_BEF.IO_CODE is '매 입 매 출 코 드  1:매 출 , 2:매 입'
/

comment on column TB_TAX_BILL_INFO_BEF.ISSUE_DAY is '작 성 일 자'
/

comment on column TB_TAX_BILL_INFO_BEF.BIZ_MANAGE_ID is '사 업 자 관 리 번 호'
/

comment on column TB_TAX_BILL_INFO_BEF.SVC_MANAGE_ID is '서 비 스 관 리 번 호'
/

comment on column TB_TAX_BILL_INFO_BEF.ISSUE_DT is '발 행 일 시'
/

comment on column TB_TAX_BILL_INFO_BEF.SIGNATURE is '전 자 서 명'
/

comment on column TB_TAX_BILL_INFO_BEF.ISSUE_ID is '승인번호 (t-kepcobill,배전공사,내선계기,일반매입),(r-한전 EDI),(p-ERP매출),(o-공가매출),(q-PPA매출),(a~n-요금 온라인 매출),(ASP00000 - ASP매입(대표메일 개별메일),수기입력(ASP)겸용서식),(PAPER000-수기입력)'
/

comment on column TB_TAX_BILL_INFO_BEF.BILL_TYPE_CODE is '세 금 계 산 서 종 류'
/

comment on column TB_TAX_BILL_INFO_BEF.PURPOSE_CODE is '영 수 청 구 코 드'
/

comment on column TB_TAX_BILL_INFO_BEF.AMENDMENT_CODE is '전 자 세 금 계 산 서  수 정  사 유 코 드'
/

comment on column TB_TAX_BILL_INFO_BEF.DESCRIPTION is '비 고'
/

comment on column TB_TAX_BILL_INFO_BEF.IMPORT_DOC_ID is '수 입  신 고 서  번 호'
/

comment on column TB_TAX_BILL_INFO_BEF.IMPORT_PERIOD_START_DAY is '일 괄 발 급  시 작 일 자'
/

comment on column TB_TAX_BILL_INFO_BEF.IMPORT_PERIOD_END_DAY is '일 괄 발 급  종 료 일 자'
/

comment on column TB_TAX_BILL_INFO_BEF.IMPORT_ITEM_QUANTITY is '일 괄 발 급  수 입  총 건'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICER_PARTY_ID is '공 급 자  사 업 자 등 록 번 호'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICER_TAX_REGIST_ID is '종 사 업 장  식 별 코 드'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICER_PARTY_NAME is '공 급 업 체  사 업 체 명'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICER_CEO_NAME is '공 급 업 체  대 표 자 명'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICER_ADDR is '공 급 업 체 의  주 소'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICER_TYPE is '공 급 업 체 의  업 태'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICER_CLASS is '공 급 업 체 의  업 종'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICER_CONTACT_DEPART is '공 급 업 체  담 당 부 서'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICER_CONTACT_NAME is '공 급 업 체  담 당 자 명'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICER_CONTACT_PHONE is '공 급 업 체  담 당 자  전 화 번 호'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICER_CONTACT_EMAIL is '공 급 업 체  담 당 자  이 메 일'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_BUSINESS_TYPE_CODE is '공 급 받 는 자  사 업 자 등 록 번 호  구 분 코 드'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_PARTY_ID is '공 급 받 는 자  사 업 자 등 록 번 호'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_TAX_REGIST_ID is '공 급 받 는 자  종 사 업 장  식 별 코 드'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_PARTY_NAME is '공 급 받 는 자  사 업 체 명'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_CEO_NAME is '공 급 받 는 자  대 표 자 명'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_ADDR is '공 급 받 는 자  주 소'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_TYPE is '공 급 받 는 자  업 태'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_CLASS is '공 급 받 는 자  업 종'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_CONTACT_DEPART1 is '공 급 받 는 자  담 당 부 서'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_CONTACT_NAME1 is '공 급 받 는 자  담 당 자 명'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_CONTACT_PHONE1 is '공 급 받 는 자  담 당 자  전 화 번 호'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_CONTACT_EMAIL1 is '공 급 받 는 자  담 당 자  이 메 일'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_CONTACT_DEPART2 is '공 급 받 는 자  담 당 부 서'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_CONTACT_NAME2 is '공 급 받 는 자  담 당 자 명'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_CONTACT_PHONE2 is '공 급 받 는 자  담 당 자  전 화 번 호'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_CONTACT_EMAIL2 is '공 급 받 는 자  담 당 자  이 메 일'
/

comment on column TB_TAX_BILL_INFO_BEF.BROKER_PARTY_ID is '수 탁 사 업 자  사 업 자 등 록 번 호'
/

comment on column TB_TAX_BILL_INFO_BEF.BROKER_TAX_REGIST_ID is '수 탁 사 업 자  종 사 업 장  식 별 코 드'
/

comment on column TB_TAX_BILL_INFO_BEF.BROKER_PARTY_NAME is '수 탁 사 업 자  사 업 체 명'
/

comment on column TB_TAX_BILL_INFO_BEF.BROKER_CEO_NAME is '수 탁 사 업 자  대 표 자 명'
/

comment on column TB_TAX_BILL_INFO_BEF.BROKER_ADDR is '수 탁 사 업 자  주 소'
/

comment on column TB_TAX_BILL_INFO_BEF.BROKER_TYPE is '수 탁 사 업 자  업 태'
/

comment on column TB_TAX_BILL_INFO_BEF.BROKER_CLASS is '수 탁 사 업 자  업 종'
/

comment on column TB_TAX_BILL_INFO_BEF.BROKER_CONTACT_DEPART is '수 탁 사 업 자  담 당 부 서'
/

comment on column TB_TAX_BILL_INFO_BEF.BROKER_CONTACT_NAME is '수 탁 사 업 자  담 당 자 명'
/

comment on column TB_TAX_BILL_INFO_BEF.BROKER_CONTACT_PHONE is '수 탁 사 업 자  담 당 자  전 화 번 호'
/

comment on column TB_TAX_BILL_INFO_BEF.BROKER_CONTACT_EMAIL is '수 탁 사 업 자  담 당 자  이 메 일'
/

comment on column TB_TAX_BILL_INFO_BEF.PAYMENT_TYPE_CODE1 is '결 제 방 법 코 드'
/

comment on column TB_TAX_BILL_INFO_BEF.PAY_AMOUNT1 is '결 제 방 법 별 금 액(0 입력시 국세청신고 오류)'
/

comment on column TB_TAX_BILL_INFO_BEF.PAYMENT_TYPE_CODE2 is '결 제 방 법 코 드'
/

comment on column TB_TAX_BILL_INFO_BEF.PAY_AMOUNT2 is '결 제 방 법 별 금 액(0 입력시 국세청신고 오류)'
/

comment on column TB_TAX_BILL_INFO_BEF.PAYMENT_TYPE_CODE3 is '결 제 방 법 코 드'
/

comment on column TB_TAX_BILL_INFO_BEF.PAY_AMOUNT3 is '결 제 방 법 별 금 액(0 입력시 국세청신고 오류)'
/

comment on column TB_TAX_BILL_INFO_BEF.PAYMENT_TYPE_CODE4 is '결 제 방 법 코 드'
/

comment on column TB_TAX_BILL_INFO_BEF.PAY_AMOUNT4 is '결 제 방 법 별 금 액(0 입력시 국세청신고 오류)'
/

comment on column TB_TAX_BILL_INFO_BEF.CHARGE_TOTAL_AMOUNT is '총  공 급 가 액  합 계'
/

comment on column TB_TAX_BILL_INFO_BEF.TAX_TOTAL_AMOUNT is '총  세 액  합 계'
/

comment on column TB_TAX_BILL_INFO_BEF.GRAND_TOTAL_AMOUNT is '총 액 (공 급 가 액 +세 액 )'
/

comment on column TB_TAX_BILL_INFO_BEF.STATUS_CODE is '진 행 상 태 (01:작성완료 / 04:신고완료)'
/

comment on column TB_TAX_BILL_INFO_BEF.ELECTRONIC_REPORT_YN is '전 자 신 고 완 료 여 부 (N:신 고 대 상 ,Y:신 고 완 료 ,F:미 신 고 대 상 (미 동 의 등 ))'
/

comment on column TB_TAX_BILL_INFO_BEF.ONLINE_GUB_CODE is '온 ,오 프 라 인  구 분 코 드 (1:온 라 인 ,2:오 프 라 인 ,3:영 업 온 라 인 )'
/

comment on column TB_TAX_BILL_INFO_BEF.ADD_TAX_YN is '가 산 세  여 부'
/

comment on column TB_TAX_BILL_INFO_BEF.INVOICEE_GUB_CODE is '공 급 받 는 자  구 분 코 드 (매 입 만  사 용 . 00:한 전 , 01:남 동  03:서 부  04:남 부  05:동 서 )'
/

comment on column TB_TAX_BILL_INFO_BEF.REL_SYSTEM_ID is '연 계 시 스 템 ID'
/

comment on column TB_TAX_BILL_INFO_BEF.JOB_GUB_CODE is '업 무 구 분 코 드 (전 력 매 출 ,공 사 매 출  등 )'
/

comment on column TB_TAX_BILL_INFO_BEF.REGIST_DT is '등 록 일 시'
/

comment on column TB_TAX_BILL_INFO_BEF.MODIFY_DT is '수 정 일 시'
/

comment on column TB_TAX_BILL_INFO_BEF.REGIST_ID is '등 록 자 ID'
/

comment on column TB_TAX_BILL_INFO_BEF.MODIFY_ID is '수 정 자 ID'
/

comment on column TB_TAX_BILL_INFO_BEF.UPPER_MANAGE_ID is '부 모 관 리 번 호  : 수 정 세 금 계 산 서  발 행 시  사 용'
/

comment on column TB_TAX_BILL_INFO_BEF.ERP_SND_YN is '한 전 EDI 국 세 청 정 보 ERP전 송 유 무 (null:미 전 송 , Y:전 송 ) 참 고 로 , KEPCOBILL은  ETS_ERP_XML_INFO_TB.STATUS 사 용 '
/

create table TB_STATUS_HIST_BEF
(
    IO_CODE       VARCHAR2(1)  not null,
    ISSUE_DAY     VARCHAR2(8)  not null,
    BIZ_MANAGE_ID VARCHAR2(24) not null,
    SEQ_NO        NUMBER(2)    not null,
    AVL_END_DT    VARCHAR2(14) not null,
    AVL_BEGIN_DT  VARCHAR2(14) not null,
    STATUS_CODE   VARCHAR2(2),
    REGIST_DT     DATE,
    REGIST_ID     VARCHAR2(20),
    STATUS_DESC   VARCHAR2(100)
)
    /

comment on table TB_STATUS_HIST_BEF is '상태이력백업정보'
/

comment on column TB_STATUS_HIST_BEF.IO_CODE is '매입매출코드 1:매출, 2:매입'
/

comment on column TB_STATUS_HIST_BEF.ISSUE_DAY is '작성일자'
/

comment on column TB_STATUS_HIST_BEF.BIZ_MANAGE_ID is '사업자관리번호'
/

comment on column TB_STATUS_HIST_BEF.SEQ_NO is '물품 일련번호'
/

comment on column TB_STATUS_HIST_BEF.AVL_END_DT is '유효종료일시'
/

comment on column TB_STATUS_HIST_BEF.AVL_BEGIN_DT is '유효시작일시'
/

comment on column TB_STATUS_HIST_BEF.STATUS_CODE is '진행상태'
/

comment on column TB_STATUS_HIST_BEF.REGIST_DT is '등록일시'
/

comment on column TB_STATUS_HIST_BEF.REGIST_ID is '등록자ID'
/

comment on column TB_STATUS_HIST_BEF.STATUS_DESC is '처리상태설명:오류발생시 오류내용을 기재함'
/

create index TB_STATUS_HIST_BEF_PK
    on TB_STATUS_HIST_BEF (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, SEQ_NO)
    /

create table IF_CODE_INFO
(
    CODE_GRP_VALUE    VARCHAR2(100),
    CODE_GRP_ID       VARCHAR2(30)         not null,
    CODE              VARCHAR2(20)         not null,
    CODE_VALUE        VARCHAR2(60),
    CODE_DESC         VARCHAR2(200),
    REF1              VARCHAR2(50),
    REF2              VARCHAR2(50),
    REF3              VARCHAR2(50),
    UPPER_CODE_GRP_ID VARCHAR2(30),
    OUT_SEQ           NUMBER(3),
    MODIFY_DT         DATE default SYSDATE not null,
    EAI_STAT          CHAR,
    EAI_CDATE         VARCHAR2(14),
    EAI_UDATE         VARCHAR2(14)
)
    /

comment on table IF_CODE_INFO is '코드정보 인터페이스'
/

comment on column IF_CODE_INFO.CODE_GRP_VALUE is '코드그룹값'
/

comment on column IF_CODE_INFO.CODE_GRP_ID is '코드그룹ID'
/

comment on column IF_CODE_INFO.CODE is '코드'
/

comment on column IF_CODE_INFO.CODE_VALUE is '코드값'
/

comment on column IF_CODE_INFO.CODE_DESC is '코드설명'
/

comment on column IF_CODE_INFO.REF1 is '참고1'
/

comment on column IF_CODE_INFO.REF2 is '참고2'
/

comment on column IF_CODE_INFO.REF3 is '참고3'
/

comment on column IF_CODE_INFO.UPPER_CODE_GRP_ID is '상위코드그룹ID'
/

comment on column IF_CODE_INFO.OUT_SEQ is '출력순서'
/

comment on column IF_CODE_INFO.MODIFY_DT is '수정일시'
/

comment on column IF_CODE_INFO.EAI_STAT is 'EAI송수신상태'
/

comment on column IF_CODE_INFO.EAI_CDATE is 'EAI입력일자'
/

comment on column IF_CODE_INFO.EAI_UDATE is 'EAI변경일자'
/

create table IF_TAX_BILL_CNT_INFO
(
    ISSUE_MONTH   VARCHAR2(6)          not null,
    SI_CNT        VARCHAR2(10)         not null,
    SI_REGIST_DT  DATE default SYSDATE not null,
    REL_SYSTEM_ID VARCHAR2(10)         not null
)
    /

comment on table IF_TAX_BILL_CNT_INFO is '매출세금계산서연계집계정보'
/

comment on column IF_TAX_BILL_CNT_INFO.ISSUE_MONTH is '대상년월'
/

comment on column IF_TAX_BILL_CNT_INFO.SI_CNT is 'SI입력건수'
/

comment on column IF_TAX_BILL_CNT_INFO.SI_REGIST_DT is 'SI 입력건수 등록일'
/

comment on column IF_TAX_BILL_CNT_INFO.REL_SYSTEM_ID is '연계시스템ID K1NCIS100a 서울, K1NCIS100b 전남, K1NCIS100c 부산, K1NCIS100d 경북, K1NCIS100e 충남, K1NCIS100f 강원, K1NCIS100g 제주, K1NCIS100h 경기남, K1NCIS100i 경남, K1NCIS100j 경기북, K1NCIS100k 인천, K1NCIS100l 전북, K1NCIS100m 충북'
/

create table IF_TAX_BILL_INFO_OFF_TEMP
(
    MANAGE_ID                   VARCHAR2(24)         not null,
    ISSUE_DAY                   VARCHAR2(8)          not null,
    BILL_TYPE_CODE              VARCHAR2(4)          not null,
    PURPOSE_CODE                VARCHAR2(2),
    AMENDMENT_CODE              VARCHAR2(2),
    DESCRIPTION                 VARCHAR2(150),
    UPPER_MANAGE_ID             VARCHAR2(24),
    INVOICER_PARTY_ID           VARCHAR2(13)         not null,
    INVOICER_TAX_REGIST_ID      VARCHAR2(4),
    INVOICER_PARTY_NAME         VARCHAR2(70)         not null,
    INVOICER_CEO_NAME           VARCHAR2(30)         not null,
    INVOICER_ADDR               VARCHAR2(150),
    INVOICER_TYPE               VARCHAR2(40),
    INVOICER_CLASS              VARCHAR2(40),
    INVOICER_CONTACT_DEPART     VARCHAR2(40),
    INVOICER_CONTACT_NAME       VARCHAR2(30),
    INVOICER_CONTACT_PHONE      VARCHAR2(20),
    INVOICER_CONTACT_EMAIL      VARCHAR2(40),
    INVOICEE_BUSINESS_TYPE_CODE VARCHAR2(2)          not null,
    INVOICEE_PARTY_ID           VARCHAR2(13)         not null,
    INVOICEE_TAX_REGIST_ID      VARCHAR2(4),
    INVOICEE_PARTY_NAME         VARCHAR2(70)         not null,
    INVOICEE_CEO_NAME           VARCHAR2(30)         not null,
    INVOICEE_ADDR               VARCHAR2(150)        not null,
    INVOICEE_TYPE               VARCHAR2(40),
    INVOICEE_CLASS              VARCHAR2(40),
    INVOICEE_CONTACT_DEPART1    VARCHAR2(40),
    INVOICEE_CONTACT_NAME1      VARCHAR2(30),
    INVOICEE_CONTACT_PHONE1     VARCHAR2(20),
    INVOICEE_CONTACT_EMAIL1     VARCHAR2(40),
    INVOICEE_CONTACT_DEPART2    VARCHAR2(40),
    INVOICEE_CONTACT_NAME2      VARCHAR2(30),
    INVOICEE_CONTACT_PHONE2     VARCHAR2(20),
    INVOICEE_CONTACT_EMAIL2     VARCHAR2(40),
    PAYMENT_TYPE_CODE1          VARCHAR2(2),
    PAY_AMOUNT1                 NUMBER(18),
    PAYMENT_TYPE_CODE2          VARCHAR2(2),
    PAY_AMOUNT2                 NUMBER(18),
    PAYMENT_TYPE_CODE3          VARCHAR2(2),
    PAY_AMOUNT3                 NUMBER(18),
    PAYMENT_TYPE_CODE4          VARCHAR2(2),
    PAY_AMOUNT4                 NUMBER(18),
    CHARGE_TOTAL_AMOUNT         NUMBER(18)           not null,
    TAX_TOTAL_AMOUNT            NUMBER(18)           not null,
    GRAND_TOTAL_AMOUNT          NUMBER(18)           not null,
    JOB_GUB_CODE                VARCHAR2(6)          not null,
    REL_SYSTEM_ID               VARCHAR2(10)         not null,
    REGIST_DT                   DATE default SYSDATE not null,
    MODIFY_DT                   DATE default SYSDATE not null,
    CANCEL_DT                   DATE,
    ISSUE_ID                    VARCHAR2(24)         not null,
    ADD_TAX_YN                  CHAR,
    BILL_ISSUE_YYYYMM           VARCHAR2(6),
    ONLINE_GUB_CODE             VARCHAR2(1)
)
    /

comment on column IF_TAX_BILL_INFO_OFF_TEMP.MANAGE_ID is '사업자관리번호'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.ISSUE_DAY is '작성일자(YYYYMMDD형식): 매출기준일자'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.BILL_TYPE_CODE is '세금계산서종류'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.PURPOSE_CODE is '영수청구코드'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.AMENDMENT_CODE is '전자세금계산서 수정 사유코드'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.DESCRIPTION is '비고'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.UPPER_MANAGE_ID is '부모관리번호 : 수정세금계산서 발행시 사용'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICER_PARTY_ID is '공급자 사업자등록번호'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICER_TAX_REGIST_ID is '종사업장 식별코드'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICER_PARTY_NAME is '공급업체 사업체명'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICER_CEO_NAME is '공급업체 대표자명'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICER_ADDR is '공급업체의 주소'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICER_TYPE is '공급업체의 업태'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICER_CLASS is '공급업체의 업종'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICER_CONTACT_DEPART is '공급업체 담당부서'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICER_CONTACT_NAME is '공급업체 담당자명'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICER_CONTACT_PHONE is '공급업체 담당자 전화번호'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICER_CONTACT_EMAIL is '공급업체 담당자 이메일'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_BUSINESS_TYPE_CODE is '공급받는자 사업자등록번호 구분코드'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_PARTY_ID is '공급받는자 사업자등록번호'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_TAX_REGIST_ID is '공급받는자 종사업장 식별코드'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_PARTY_NAME is '공급받는자 사업체명'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_CEO_NAME is '공급받는자 대표자명'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_ADDR is '공급받는자 주소'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_TYPE is '공급받는자 업태'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_CLASS is '공급받는자 업종'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_CONTACT_DEPART1 is '공급받는자 담당부서'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_CONTACT_NAME1 is '공급받는자 담당자명'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_CONTACT_PHONE1 is '공급받는자 담당자 전화번호'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_CONTACT_EMAIL1 is '공급받는자 담당자 이메일'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_CONTACT_DEPART2 is '공급받는자 담당부서'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_CONTACT_NAME2 is '공급받는자 담당자명'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_CONTACT_PHONE2 is '공급받는자 담당자 전화번호'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.INVOICEE_CONTACT_EMAIL2 is '공급받는자 담당자 이메일'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.PAYMENT_TYPE_CODE1 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.PAY_AMOUNT1 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.PAYMENT_TYPE_CODE2 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.PAY_AMOUNT2 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.PAYMENT_TYPE_CODE3 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.PAY_AMOUNT3 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.PAYMENT_TYPE_CODE4 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.PAY_AMOUNT4 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.CHARGE_TOTAL_AMOUNT is '총 공급가액 합계'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.TAX_TOTAL_AMOUNT is '총 세액 합계'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.GRAND_TOTAL_AMOUNT is '총액(공급가액+세액)'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.JOB_GUB_CODE is '업무구분코드(전력매출,공사매출 등)'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.REL_SYSTEM_ID is '연계시스템ID'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.REGIST_DT is '등록일시'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.MODIFY_DT is '수정일시'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.CANCEL_DT is '취소일시'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.ISSUE_ID is '승인번호'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.ADD_TAX_YN is '가산세 여부'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.BILL_ISSUE_YYYYMM is '청구서발행년월'
/

comment on column IF_TAX_BILL_INFO_OFF_TEMP.ONLINE_GUB_CODE is '온,오프라인 구분코드(1:온라인,2:오프라인)'
/

create index IF_TAX_BILL_INFO_OFF_TEMP_IX1
    on IF_TAX_BILL_INFO_OFF_TEMP (ONLINE_GUB_CODE, MODIFY_DT, ISSUE_DAY)
    /

create index IF_TAX_BILL_INFO_OFF_TEMP_IX2
    on IF_TAX_BILL_INFO_OFF_TEMP (JOB_GUB_CODE, REL_SYSTEM_ID, ISSUE_ID, ISSUE_DAY)
    /

create unique index IF_TAX_BILL_INFO_OFF_TEMP_PK
    on IF_TAX_BILL_INFO_OFF_TEMP (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID)
    /

create index IF_TAX_BILL_INFO_OFF_TEMP_IX3
    on IF_TAX_BILL_INFO_OFF_TEMP (ISSUE_ID)
    /

alter table IF_TAX_BILL_INFO_OFF_TEMP
    add primary key (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID)
    /

create table TB_TAX_BILL_INFO
(
    IO_CODE                     VARCHAR2(1)                 not null,
    ISSUE_DAY                   VARCHAR2(8)                 not null,
    BIZ_MANAGE_ID               VARCHAR2(24)                not null,
    SVC_MANAGE_ID               VARCHAR2(24),
    ISSUE_DT                    VARCHAR2(14),
    SIGNATURE                   VARCHAR2(1024),
    ISSUE_ID                    VARCHAR2(24)                not null,
    BILL_TYPE_CODE              VARCHAR2(4)                 not null,
    PURPOSE_CODE                VARCHAR2(2),
    AMENDMENT_CODE              VARCHAR2(2),
    DESCRIPTION                 VARCHAR2(150),
    IMPORT_DOC_ID               VARCHAR2(15),
    IMPORT_PERIOD_START_DAY     VARCHAR2(8),
    IMPORT_PERIOD_END_DAY       VARCHAR2(8),
    IMPORT_ITEM_QUANTITY        NUMBER(6),
    INVOICER_PARTY_ID           VARCHAR2(13)                not null,
    INVOICER_TAX_REGIST_ID      VARCHAR2(4),
    INVOICER_PARTY_NAME         VARCHAR2(70)                not null,
    INVOICER_CEO_NAME           VARCHAR2(30)                not null,
    INVOICER_ADDR               VARCHAR2(150),
    INVOICER_TYPE               VARCHAR2(40),
    INVOICER_CLASS              VARCHAR2(40),
    INVOICER_CONTACT_DEPART     VARCHAR2(40),
    INVOICER_CONTACT_NAME       VARCHAR2(30),
    INVOICER_CONTACT_PHONE      VARCHAR2(20),
    INVOICER_CONTACT_EMAIL      VARCHAR2(40),
    INVOICEE_BUSINESS_TYPE_CODE VARCHAR2(2)                 not null,
    INVOICEE_PARTY_ID           VARCHAR2(13)                not null,
    INVOICEE_TAX_REGIST_ID      VARCHAR2(4),
    INVOICEE_PARTY_NAME         VARCHAR2(70)                not null,
    INVOICEE_CEO_NAME           VARCHAR2(30)                not null,
    INVOICEE_ADDR               VARCHAR2(150),
    INVOICEE_TYPE               VARCHAR2(40),
    INVOICEE_CLASS              VARCHAR2(40),
    INVOICEE_CONTACT_DEPART1    VARCHAR2(40),
    INVOICEE_CONTACT_NAME1      VARCHAR2(30),
    INVOICEE_CONTACT_PHONE1     VARCHAR2(20),
    INVOICEE_CONTACT_EMAIL1     VARCHAR2(40),
    INVOICEE_CONTACT_DEPART2    VARCHAR2(40),
    INVOICEE_CONTACT_NAME2      VARCHAR2(30),
    INVOICEE_CONTACT_PHONE2     VARCHAR2(20),
    INVOICEE_CONTACT_EMAIL2     VARCHAR2(40),
    BROKER_PARTY_ID             VARCHAR2(13),
    BROKER_TAX_REGIST_ID        VARCHAR2(4),
    BROKER_PARTY_NAME           VARCHAR2(70),
    BROKER_CEO_NAME             VARCHAR2(30),
    BROKER_ADDR                 VARCHAR2(150),
    BROKER_TYPE                 VARCHAR2(40),
    BROKER_CLASS                VARCHAR2(40),
    BROKER_CONTACT_DEPART       VARCHAR2(40),
    BROKER_CONTACT_NAME         VARCHAR2(30),
    BROKER_CONTACT_PHONE        VARCHAR2(20),
    BROKER_CONTACT_EMAIL        VARCHAR2(40),
    PAYMENT_TYPE_CODE1          VARCHAR2(2),
    PAY_AMOUNT1                 NUMBER(18),
    PAYMENT_TYPE_CODE2          VARCHAR2(2),
    PAY_AMOUNT2                 NUMBER(18),
    PAYMENT_TYPE_CODE3          VARCHAR2(2),
    PAY_AMOUNT3                 NUMBER(18),
    PAYMENT_TYPE_CODE4          VARCHAR2(2),
    PAY_AMOUNT4                 NUMBER(18),
    CHARGE_TOTAL_AMOUNT         NUMBER(18)                  not null,
    TAX_TOTAL_AMOUNT            NUMBER(18)                  not null,
    GRAND_TOTAL_AMOUNT          NUMBER(18)                  not null,
    STATUS_CODE                 VARCHAR2(2),
    ELECTRONIC_REPORT_YN        VARCHAR2(1) default 'N'     not null,
    ONLINE_GUB_CODE             VARCHAR2(1),
    ADD_TAX_YN                  VARCHAR2(1) default 'N'     not null,
    INVOICEE_GUB_CODE           VARCHAR2(2),
    REL_SYSTEM_ID               VARCHAR2(10),
    JOB_GUB_CODE                VARCHAR2(6),
    REGIST_DT                   DATE        default SYSDATE not null,
    MODIFY_DT                   DATE        default SYSDATE not null,
    REGIST_ID                   VARCHAR2(20),
    MODIFY_ID                   VARCHAR2(30),
    UPPER_MANAGE_ID             VARCHAR2(24),
    ERP_SND_YN                  CHAR,
    constraint TB_TAX_BILL_INFO_PK
        primary key (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID)
)
    /

comment on table TB_TAX_BILL_INFO is '세 금 계 산 서  기 본  정 보'
/

comment on column TB_TAX_BILL_INFO.IO_CODE is '매 입 매 출 코 드  1:매 출 , 2:매 입'
/

comment on column TB_TAX_BILL_INFO.ISSUE_DAY is '작 성 일 자'
/

comment on column TB_TAX_BILL_INFO.BIZ_MANAGE_ID is '사 업 자 관 리 번 호'
/

comment on column TB_TAX_BILL_INFO.SVC_MANAGE_ID is '서 비 스 관 리 번 호'
/

comment on column TB_TAX_BILL_INFO.ISSUE_DT is '발 행 일 시'
/

comment on column TB_TAX_BILL_INFO.SIGNATURE is '전 자 서 명'
/

comment on column TB_TAX_BILL_INFO.ISSUE_ID is '승인번호 (t-kepcobill,배전공사,내선계기,일반매입),(r-한전 EDI),(p-ERP매출),(o-공가매출),(q-PPA매출),(a~n-요금 온라인 매출),(ASP00000 - ASP매입(대표메일 개별메일),수기입력(ASP)겸용서식),(PAPER000-수기입력)'
/

comment on column TB_TAX_BILL_INFO.BILL_TYPE_CODE is '세 금 계 산 서 종 류'
/

comment on column TB_TAX_BILL_INFO.PURPOSE_CODE is '영 수 청 구 코 드'
/

comment on column TB_TAX_BILL_INFO.AMENDMENT_CODE is '전 자 세 금 계 산 서  수 정  사 유 코 드'
/

comment on column TB_TAX_BILL_INFO.DESCRIPTION is '비 고'
/

comment on column TB_TAX_BILL_INFO.IMPORT_DOC_ID is '수 입  신 고 서  번 호'
/

comment on column TB_TAX_BILL_INFO.IMPORT_PERIOD_START_DAY is '일 괄 발 급  시 작 일 자'
/

comment on column TB_TAX_BILL_INFO.IMPORT_PERIOD_END_DAY is '일 괄 발 급  종 료 일 자'
/

comment on column TB_TAX_BILL_INFO.IMPORT_ITEM_QUANTITY is '일 괄 발 급  수 입  총 건'
/

comment on column TB_TAX_BILL_INFO.INVOICER_PARTY_ID is '공 급 자  사 업 자 등 록 번 호'
/

comment on column TB_TAX_BILL_INFO.INVOICER_TAX_REGIST_ID is '종 사 업 장  식 별 코 드'
/

comment on column TB_TAX_BILL_INFO.INVOICER_PARTY_NAME is '공 급 업 체  사 업 체 명'
/

comment on column TB_TAX_BILL_INFO.INVOICER_CEO_NAME is '공 급 업 체  대 표 자 명'
/

comment on column TB_TAX_BILL_INFO.INVOICER_ADDR is '공 급 업 체 의  주 소'
/

comment on column TB_TAX_BILL_INFO.INVOICER_TYPE is '공 급 업 체 의  업 태'
/

comment on column TB_TAX_BILL_INFO.INVOICER_CLASS is '공 급 업 체 의  업 종'
/

comment on column TB_TAX_BILL_INFO.INVOICER_CONTACT_DEPART is '공 급 업 체  담 당 부 서'
/

comment on column TB_TAX_BILL_INFO.INVOICER_CONTACT_NAME is '공 급 업 체  담 당 자 명'
/

comment on column TB_TAX_BILL_INFO.INVOICER_CONTACT_PHONE is '공 급 업 체  담 당 자  전 화 번 호'
/

comment on column TB_TAX_BILL_INFO.INVOICER_CONTACT_EMAIL is '공 급 업 체  담 당 자  이 메 일'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_BUSINESS_TYPE_CODE is '공 급 받 는 자  사 업 자 등 록 번 호  구 분 코 드'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_PARTY_ID is '공 급 받 는 자  사 업 자 등 록 번 호'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_TAX_REGIST_ID is '공 급 받 는 자  종 사 업 장  식 별 코 드'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_PARTY_NAME is '공 급 받 는 자  사 업 체 명'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_CEO_NAME is '공 급 받 는 자  대 표 자 명'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_ADDR is '공 급 받 는 자  주 소'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_TYPE is '공 급 받 는 자  업 태'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_CLASS is '공 급 받 는 자  업 종'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_CONTACT_DEPART1 is '공 급 받 는 자  담 당 부 서'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_CONTACT_NAME1 is '공 급 받 는 자  담 당 자 명'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_CONTACT_PHONE1 is '공 급 받 는 자  담 당 자  전 화 번 호'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_CONTACT_EMAIL1 is '공 급 받 는 자  담 당 자  이 메 일'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_CONTACT_DEPART2 is '공 급 받 는 자  담 당 부 서'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_CONTACT_NAME2 is '공 급 받 는 자  담 당 자 명'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_CONTACT_PHONE2 is '공 급 받 는 자  담 당 자  전 화 번 호'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_CONTACT_EMAIL2 is '공 급 받 는 자  담 당 자  이 메 일'
/

comment on column TB_TAX_BILL_INFO.BROKER_PARTY_ID is '수 탁 사 업 자  사 업 자 등 록 번 호'
/

comment on column TB_TAX_BILL_INFO.BROKER_TAX_REGIST_ID is '수 탁 사 업 자  종 사 업 장  식 별 코 드'
/

comment on column TB_TAX_BILL_INFO.BROKER_PARTY_NAME is '수 탁 사 업 자  사 업 체 명'
/

comment on column TB_TAX_BILL_INFO.BROKER_CEO_NAME is '수 탁 사 업 자  대 표 자 명'
/

comment on column TB_TAX_BILL_INFO.BROKER_ADDR is '수 탁 사 업 자  주 소'
/

comment on column TB_TAX_BILL_INFO.BROKER_TYPE is '수 탁 사 업 자  업 태'
/

comment on column TB_TAX_BILL_INFO.BROKER_CLASS is '수 탁 사 업 자  업 종'
/

comment on column TB_TAX_BILL_INFO.BROKER_CONTACT_DEPART is '수 탁 사 업 자  담 당 부 서'
/

comment on column TB_TAX_BILL_INFO.BROKER_CONTACT_NAME is '수 탁 사 업 자  담 당 자 명'
/

comment on column TB_TAX_BILL_INFO.BROKER_CONTACT_PHONE is '수 탁 사 업 자  담 당 자  전 화 번 호'
/

comment on column TB_TAX_BILL_INFO.BROKER_CONTACT_EMAIL is '수 탁 사 업 자  담 당 자  이 메 일'
/

comment on column TB_TAX_BILL_INFO.PAYMENT_TYPE_CODE1 is '결 제 방 법 코 드'
/

comment on column TB_TAX_BILL_INFO.PAY_AMOUNT1 is '결 제 방 법 별 금 액(0 입력시 국세청신고 오류)'
/

comment on column TB_TAX_BILL_INFO.PAYMENT_TYPE_CODE2 is '결 제 방 법 코 드'
/

comment on column TB_TAX_BILL_INFO.PAY_AMOUNT2 is '결 제 방 법 별 금 액(0 입력시 국세청신고 오류)'
/

comment on column TB_TAX_BILL_INFO.PAYMENT_TYPE_CODE3 is '결 제 방 법 코 드'
/

comment on column TB_TAX_BILL_INFO.PAY_AMOUNT3 is '결 제 방 법 별 금 액(0 입력시 국세청신고 오류)'
/

comment on column TB_TAX_BILL_INFO.PAYMENT_TYPE_CODE4 is '결 제 방 법 코 드'
/

comment on column TB_TAX_BILL_INFO.PAY_AMOUNT4 is '결 제 방 법 별 금 액(0 입력시 국세청신고 오류)'
/

comment on column TB_TAX_BILL_INFO.CHARGE_TOTAL_AMOUNT is '총  공 급 가 액  합 계'
/

comment on column TB_TAX_BILL_INFO.TAX_TOTAL_AMOUNT is '총  세 액  합 계'
/

comment on column TB_TAX_BILL_INFO.GRAND_TOTAL_AMOUNT is '총 액 (공 급 가 액 +세 액 )'
/

comment on column TB_TAX_BILL_INFO.STATUS_CODE is '진 행 상 태 (01:작성완료 / 04:신고완료)'
/

comment on column TB_TAX_BILL_INFO.ELECTRONIC_REPORT_YN is '전 자 신 고 완 료 여 부 (N:신 고 대 상 ,Y:신 고 완 료 ,F:미 신 고 대 상 (미 동 의 등 ))'
/

comment on column TB_TAX_BILL_INFO.ONLINE_GUB_CODE is '온 ,오 프 라 인  구 분 코 드 (1:온 라 인 ,2:오 프 라 인 ,3:영 업 온 라 인 )'
/

comment on column TB_TAX_BILL_INFO.ADD_TAX_YN is '가 산 세  여 부'
/

comment on column TB_TAX_BILL_INFO.INVOICEE_GUB_CODE is '공 급 받 는 자  구 분 코 드 (매 입 만  사 용 . 00:한 전 , 01:남 동  03:서 부  04:남 부  05:동 서 )'
/

comment on column TB_TAX_BILL_INFO.REL_SYSTEM_ID is '연 계 시 스 템 ID'
/

comment on column TB_TAX_BILL_INFO.JOB_GUB_CODE is '업 무 구 분 코 드 (전 력 매 출 ,공 사 매 출  등 )'
/

comment on column TB_TAX_BILL_INFO.REGIST_DT is '등 록 일 시'
/

comment on column TB_TAX_BILL_INFO.MODIFY_DT is '수 정 일 시'
/

comment on column TB_TAX_BILL_INFO.REGIST_ID is '등 록 자 ID'
/

comment on column TB_TAX_BILL_INFO.MODIFY_ID is '수 정 자 ID'
/

comment on column TB_TAX_BILL_INFO.UPPER_MANAGE_ID is '부 모 관 리 번 호  : 수 정 세 금 계 산 서  발 행 시  사 용'
/

comment on column TB_TAX_BILL_INFO.ERP_SND_YN is '한 전 EDI 국 세 청 정 보 ERP전 송 유 무 (null:미 전 송 , Y:전 송 ) 참 고 로 , KEPCOBILL은  ETS_ERP_XML_INFO_TB.STATUS 사 용 '
/

create index TB_TAX_BILL_INFO_IX1
    on TB_TAX_BILL_INFO (IO_CODE, ONLINE_GUB_CODE, STATUS_CODE, ISSUE_DAY)
    /

create index TB_TAX_BILL_INFO_IX2
    on TB_TAX_BILL_INFO (ONLINE_GUB_CODE, ELECTRONIC_REPORT_YN, ISSUE_DAY)
    /

create index TB_TAX_BILL_INFO_IX3
    on TB_TAX_BILL_INFO (INVOICEE_PARTY_ID, ISSUE_DAY)
    /

create unique index TB_TAX_BILL_INFO_UK
    on TB_TAX_BILL_INFO (ISSUE_ID)
    /

create table TB_TRADE_ITEM_LIST
(
    IO_CODE        VARCHAR2(1)  not null,
    ISSUE_DAY      VARCHAR2(8)  not null,
    BIZ_MANAGE_ID  VARCHAR2(24) not null,
    SEQ_NO         NUMBER(2)    not null,
    PURCHASE_DAY   VARCHAR2(8),
    ITEM_NAME      VARCHAR2(100),
    ITEM_INFO      VARCHAR2(60),
    ITEM_DESC      VARCHAR2(100),
    UNIT_QUANTITY  NUMBER(10, 2),
    UNIT_AMOUNT    NUMBER(16, 2),
    INVOICE_AMOUNT NUMBER(18),
    TAX_AMOUNT     NUMBER(18),
    constraint TB_TRADE_ITEM_LIST_PK
        primary key (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, SEQ_NO)
)
    /

comment on table TB_TRADE_ITEM_LIST is '매출세금계산서 품목 정보'
/

comment on column TB_TRADE_ITEM_LIST.IO_CODE is '매입매출코드 1:매출, 2:매입'
/

comment on column TB_TRADE_ITEM_LIST.ISSUE_DAY is '작성일자'
/

comment on column TB_TRADE_ITEM_LIST.BIZ_MANAGE_ID is '사업자관리번호'
/

comment on column TB_TRADE_ITEM_LIST.SEQ_NO is '물품 일련번호'
/

comment on column TB_TRADE_ITEM_LIST.PURCHASE_DAY is '물품 공급일자'
/

comment on column TB_TRADE_ITEM_LIST.ITEM_NAME is '물품명'
/

comment on column TB_TRADE_ITEM_LIST.ITEM_INFO is '물품에 대한 규격'
/

comment on column TB_TRADE_ITEM_LIST.ITEM_DESC is '물품과 관련된 자유기술문'
/

comment on column TB_TRADE_ITEM_LIST.UNIT_QUANTITY is '수량'
/

comment on column TB_TRADE_ITEM_LIST.UNIT_AMOUNT is '물품 단가'
/

comment on column TB_TRADE_ITEM_LIST.INVOICE_AMOUNT is '물품 공급 가액'
/

comment on column TB_TRADE_ITEM_LIST.TAX_AMOUNT is '물품 세액'
/

create table IF_TAX_BILL_INFO_SMP
(
    REL_SYSTEM_ID               VARCHAR2(10)  not null,
    JOB_GUB_CODE                VARCHAR2(6)   not null,
    MANAGE_ID                   VARCHAR2(24)  not null,
    ADD_TAX_YN                  VARCHAR2(1),
    ISSUE_ID                    VARCHAR2(24)  not null,
    ISSUE_DAY                   VARCHAR2(8)   not null,
    BILL_TYPE_CODE              VARCHAR2(4)   not null,
    PURPOSE_CODE                VARCHAR2(2),
    AMENDMENT_CODE              VARCHAR2(2),
    UPPER_MANAGE_ID             VARCHAR2(24),
    DESCRIPTION                 VARCHAR2(150),
    INVOICER_PARTY_ID           VARCHAR2(13)  not null,
    INVOICER_TAX_REGIST_ID      VARCHAR2(4),
    INVOICER_PARTY_NAME         VARCHAR2(70)  not null,
    INVOICER_CEO_NAME           VARCHAR2(30)  not null,
    INVOICER_ADDR               VARCHAR2(150),
    INVOICER_TYPE               VARCHAR2(40),
    INVOICER_CLASS              VARCHAR2(40),
    INVOICER_CONTACT_DEPART     VARCHAR2(40),
    INVOICER_CONTACT_NAME       VARCHAR2(30),
    INVOICER_CONTACT_PHONE      VARCHAR2(20),
    INVOICER_CONTACT_EMAIL      VARCHAR2(40),
    INVOICEE_BUSINESS_TYPE_CODE VARCHAR2(2)   not null,
    INVOICEE_PARTY_ID           VARCHAR2(13)  not null,
    INVOICEE_TAX_REGIST_ID      VARCHAR2(4),
    INVOICEE_PARTY_NAME         VARCHAR2(70)  not null,
    INVOICEE_CEO_NAME           VARCHAR2(30)  not null,
    INVOICEE_ADDR               VARCHAR2(150) not null,
    INVOICEE_TYPE               VARCHAR2(40),
    INVOICEE_CLASS              VARCHAR2(40),
    INVOICEE_CONTACT_DEPART1    VARCHAR2(40),
    INVOICEE_CONTACT_NAME1      VARCHAR2(30),
    INVOICEE_CONTACT_PHONE1     VARCHAR2(20),
    INVOICEE_CONTACT_EMAIL1     VARCHAR2(40),
    INVOICEE_CONTACT_DEPART2    VARCHAR2(40),
    INVOICEE_CONTACT_NAME2      VARCHAR2(30),
    INVOICEE_CONTACT_PHONE2     VARCHAR2(20),
    INVOICEE_CONTACT_EMAIL2     VARCHAR2(40),
    PAYMENT_TYPE_CODE1          VARCHAR2(2)   not null,
    PAY_AMOUNT1                 NUMBER(18)    not null,
    PAYMENT_TYPE_CODE2          VARCHAR2(2),
    PAY_AMOUNT2                 NUMBER(18),
    PAYMENT_TYPE_CODE3          VARCHAR2(2),
    PAY_AMOUNT3                 NUMBER(18),
    PAYMENT_TYPE_CODE4          VARCHAR2(2),
    PAY_AMOUNT4                 NUMBER(18),
    CHARGE_TOTAL_AMOUNT         NUMBER(18)    not null,
    TAX_TOTAL_AMOUNT            NUMBER(18)    not null,
    GRAND_TOTAL_AMOUNT          NUMBER(18)    not null,
    CUST_NO                     VARCHAR2(10),
    BILL_YYYYMM                 VARCHAR2(6),
    BILL_ISSUE_YYYYMM           VARCHAR2(6),
    PAY_DEADLINE                VARCHAR2(8),
    ONLINE_GUB_CODE             VARCHAR2(1)   not null,
    FLG                         CHAR          not null,
    REGIST_DT                   DATE          not null,
    MODIFY_DT                   DATE          not null,
    CANCEL_DT                   DATE,
    EAI_STAT                    CHAR,
    EAI_CDATE                   VARCHAR2(14),
    EAI_UDATE                   VARCHAR2(14),
    GRP_NO                      CHAR(2)
)
    /

comment on table IF_TAX_BILL_INFO_SMP is '업무시스템연계 매출세금계산서(매입증빙포함) 배치 정보'
/

comment on column IF_TAX_BILL_INFO_SMP.REL_SYSTEM_ID is '연계시스템ID'
/

comment on column IF_TAX_BILL_INFO_SMP.JOB_GUB_CODE is '업무구분코드(전력매출,공사매출 등)'
/

comment on column IF_TAX_BILL_INFO_SMP.MANAGE_ID is '사업자관리번호'
/

comment on column IF_TAX_BILL_INFO_SMP.ADD_TAX_YN is '가산세 여부'
/

comment on column IF_TAX_BILL_INFO_SMP.ISSUE_ID is '승인번호'
/

comment on column IF_TAX_BILL_INFO_SMP.ISSUE_DAY is '작성일자(YYYYMMDD형식): 매출기준일자'
/

comment on column IF_TAX_BILL_INFO_SMP.BILL_TYPE_CODE is '세금계산서종류'
/

comment on column IF_TAX_BILL_INFO_SMP.PURPOSE_CODE is '영수청구코드'
/

comment on column IF_TAX_BILL_INFO_SMP.AMENDMENT_CODE is '전자세금계산서 수정 사유코드'
/

comment on column IF_TAX_BILL_INFO_SMP.UPPER_MANAGE_ID is '부모관리번호 : 수정세금계산서 발행시 사용'
/

comment on column IF_TAX_BILL_INFO_SMP.DESCRIPTION is '비고'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICER_PARTY_ID is '공급자 사업자등록번호'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICER_TAX_REGIST_ID is '종사업장 식별코드'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICER_PARTY_NAME is '공급업체 사업체명'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICER_CEO_NAME is '공급업체 대표자명'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICER_ADDR is '공급업체의 주소'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICER_TYPE is '공급업체의 업태'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICER_CLASS is '공급업체의 업종'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICER_CONTACT_DEPART is '공급업체 담당부서'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICER_CONTACT_NAME is '공급업체 담당자명'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICER_CONTACT_PHONE is '공급업체 담당자 전화번호'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICER_CONTACT_EMAIL is '공급업체 담당자 이메일'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_BUSINESS_TYPE_CODE is '공급받는자 사업자등록번호 구분코드'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_PARTY_ID is '공급받는자 사업자등록번호'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_TAX_REGIST_ID is '공급받는자 종사업장 식별코드'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_PARTY_NAME is '공급받는자 사업체명'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_CEO_NAME is '공급받는자 대표자명'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_ADDR is '공급받는자 주소'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_TYPE is '공급받는자 업태'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_CLASS is '공급받는자 업종'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_CONTACT_DEPART1 is '공급받는자 담당부서'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_CONTACT_NAME1 is '공급받는자 담당자명'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_CONTACT_PHONE1 is '공급받는자 담당자 전화번호'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_CONTACT_EMAIL1 is '공급받는자 담당자 이메일'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_CONTACT_DEPART2 is '공급받는자 담당부서'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_CONTACT_NAME2 is '공급받는자 담당자명'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_CONTACT_PHONE2 is '공급받는자 담당자 전화번호'
/

comment on column IF_TAX_BILL_INFO_SMP.INVOICEE_CONTACT_EMAIL2 is '공급받는자 담당자 이메일'
/

comment on column IF_TAX_BILL_INFO_SMP.PAYMENT_TYPE_CODE1 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO_SMP.PAY_AMOUNT1 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO_SMP.PAYMENT_TYPE_CODE2 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO_SMP.PAY_AMOUNT2 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO_SMP.PAYMENT_TYPE_CODE3 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO_SMP.PAY_AMOUNT3 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO_SMP.PAYMENT_TYPE_CODE4 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO_SMP.PAY_AMOUNT4 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO_SMP.CHARGE_TOTAL_AMOUNT is '총 공급가액 합계'
/

comment on column IF_TAX_BILL_INFO_SMP.TAX_TOTAL_AMOUNT is '총 세액 합계'
/

comment on column IF_TAX_BILL_INFO_SMP.GRAND_TOTAL_AMOUNT is '총액(공급가액+세액)'
/

comment on column IF_TAX_BILL_INFO_SMP.CUST_NO is '전기고객번호'
/

comment on column IF_TAX_BILL_INFO_SMP.BILL_YYYYMM is '청구년월'
/

comment on column IF_TAX_BILL_INFO_SMP.BILL_ISSUE_YYYYMM is '청구서발행년월'
/

comment on column IF_TAX_BILL_INFO_SMP.PAY_DEADLINE is '지급만기일자'
/

comment on column IF_TAX_BILL_INFO_SMP.ONLINE_GUB_CODE is '온,오프라인 구분코드(1:온라인,2:오프라인, 3:요금온라인)'
/

comment on column IF_TAX_BILL_INFO_SMP.FLG is '처리구분(Y,N)'
/

comment on column IF_TAX_BILL_INFO_SMP.REGIST_DT is '등록일시'
/

comment on column IF_TAX_BILL_INFO_SMP.MODIFY_DT is '수정일시'
/

comment on column IF_TAX_BILL_INFO_SMP.CANCEL_DT is '취소일시'
/

comment on column IF_TAX_BILL_INFO_SMP.EAI_STAT is 'p:진행중 c:완료 e:에러'
/

comment on column IF_TAX_BILL_INFO_SMP.EAI_CDATE is 'EAI 연계 생성일시'
/

comment on column IF_TAX_BILL_INFO_SMP.EAI_UDATE is 'EAI 연계 갱신일시'
/

comment on column IF_TAX_BILL_INFO_SMP.GRP_NO is '배치분할차수'
/

create table IF_TAX_BILL_ITEM_LIST_SMP
(
    REL_SYSTEM_ID     VARCHAR2(10)  not null,
    JOB_GUB_CODE      VARCHAR2(6)   not null,
    MANAGE_ID         VARCHAR2(24)  not null,
    SEQ_NO            NUMBER(2)     not null,
    PURCHASE_DAY      VARCHAR2(8)   not null,
    ITEM_NAME         VARCHAR2(100) not null,
    ITEM_INFO         VARCHAR2(60),
    ITEM_DESC         VARCHAR2(100),
    UNIT_QUANTITY     NUMBER(10, 2) not null,
    UNIT_AMOUNT       NUMBER(16, 2) not null,
    INVOICE_AMOUNT    NUMBER(18)    not null,
    TAX_AMOUNT        NUMBER(18)    not null,
    BILL_ISSUE_YYYYMM VARCHAR2(6)
)
    /

comment on table IF_TAX_BILL_ITEM_LIST_SMP is '업무시스템연계 매출세금계산서 품목 배치 정보'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.REL_SYSTEM_ID is '연계시스템ID'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.JOB_GUB_CODE is '업무구분코드(전력매출,공사매출 등)'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.MANAGE_ID is '사업자관리번호'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.SEQ_NO is '물품 일련번호'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.PURCHASE_DAY is '물품 공급일자'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.ITEM_NAME is '물품명'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.ITEM_INFO is '물품에 대한 규격'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.ITEM_DESC is '물품과 관련된 자유기술문'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.UNIT_QUANTITY is '수량'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.UNIT_AMOUNT is '물품 단가'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.INVOICE_AMOUNT is '물품 공급 가액'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.TAX_AMOUNT is '물품 세액'
/

comment on column IF_TAX_BILL_ITEM_LIST_SMP.BILL_ISSUE_YYYYMM is '청구발행년월'
/

create table IF_TAX_BILL_INFO
(
    REL_SYSTEM_ID               VARCHAR2(10)  not null,
    JOB_GUB_CODE                VARCHAR2(6)   not null,
    MANAGE_ID                   VARCHAR2(24)  not null,
    ADD_TAX_YN                  VARCHAR2(1),
    ISSUE_ID                    VARCHAR2(24)  not null,
    ISSUE_DAY                   VARCHAR2(8)   not null,
    BILL_TYPE_CODE              VARCHAR2(4)   not null,
    PURPOSE_CODE                VARCHAR2(2),
    AMENDMENT_CODE              VARCHAR2(2),
    UPPER_MANAGE_ID             VARCHAR2(24),
    DESCRIPTION                 VARCHAR2(150),
    INVOICER_PARTY_ID           VARCHAR2(13)  not null,
    INVOICER_TAX_REGIST_ID      VARCHAR2(4),
    INVOICER_PARTY_NAME         VARCHAR2(70)  not null,
    INVOICER_CEO_NAME           VARCHAR2(30)  not null,
    INVOICER_ADDR               VARCHAR2(150),
    INVOICER_TYPE               VARCHAR2(40),
    INVOICER_CLASS              VARCHAR2(40),
    INVOICER_CONTACT_DEPART     VARCHAR2(40),
    INVOICER_CONTACT_NAME       VARCHAR2(30),
    INVOICER_CONTACT_PHONE      VARCHAR2(20),
    INVOICER_CONTACT_EMAIL      VARCHAR2(40),
    INVOICEE_BUSINESS_TYPE_CODE VARCHAR2(2)   not null,
    INVOICEE_PARTY_ID           VARCHAR2(13)  not null,
    INVOICEE_TAX_REGIST_ID      VARCHAR2(4),
    INVOICEE_PARTY_NAME         VARCHAR2(70)  not null,
    INVOICEE_CEO_NAME           VARCHAR2(30)  not null,
    INVOICEE_ADDR               VARCHAR2(150) not null,
    INVOICEE_TYPE               VARCHAR2(40),
    INVOICEE_CLASS              VARCHAR2(40),
    INVOICEE_CONTACT_DEPART1    VARCHAR2(40),
    INVOICEE_CONTACT_NAME1      VARCHAR2(30),
    INVOICEE_CONTACT_PHONE1     VARCHAR2(20),
    INVOICEE_CONTACT_EMAIL1     VARCHAR2(40),
    INVOICEE_CONTACT_DEPART2    VARCHAR2(40),
    INVOICEE_CONTACT_NAME2      VARCHAR2(30),
    INVOICEE_CONTACT_PHONE2     VARCHAR2(20),
    INVOICEE_CONTACT_EMAIL2     VARCHAR2(40),
    PAYMENT_TYPE_CODE1          VARCHAR2(2)   not null,
    PAY_AMOUNT1                 NUMBER(18)    not null,
    PAYMENT_TYPE_CODE2          VARCHAR2(2),
    PAY_AMOUNT2                 NUMBER(18),
    PAYMENT_TYPE_CODE3          VARCHAR2(2),
    PAY_AMOUNT3                 NUMBER(18),
    PAYMENT_TYPE_CODE4          VARCHAR2(2),
    PAY_AMOUNT4                 NUMBER(18),
    CHARGE_TOTAL_AMOUNT         NUMBER(18)    not null,
    TAX_TOTAL_AMOUNT            NUMBER(18)    not null,
    GRAND_TOTAL_AMOUNT          NUMBER(18)    not null,
    CUST_NO                     VARCHAR2(10),
    BILL_YYYYMM                 VARCHAR2(6),
    BILL_ISSUE_YYYYMM           VARCHAR2(6),
    PAY_DEADLINE                VARCHAR2(8),
    ONLINE_GUB_CODE             VARCHAR2(1)   not null,
    FLG                         CHAR          not null,
    REGIST_DT                   DATE          not null,
    MODIFY_DT                   DATE          not null,
    CANCEL_DT                   DATE,
    EAI_STAT                    CHAR,
    EAI_CDATE                   VARCHAR2(14),
    EAI_UDATE                   VARCHAR2(14),
    GRP_NO                      CHAR(2)
)
    /

comment on table IF_TAX_BILL_INFO is '업무시스템연계 매출세금계산서 기본 정보'
/

comment on column IF_TAX_BILL_INFO.REL_SYSTEM_ID is '연계시스템ID'
/

comment on column IF_TAX_BILL_INFO.JOB_GUB_CODE is '업무구분코드(전력매출,공사매출 등)'
/

comment on column IF_TAX_BILL_INFO.MANAGE_ID is '사업자관리번호'
/

comment on column IF_TAX_BILL_INFO.ADD_TAX_YN is '가산세 여부'
/

comment on column IF_TAX_BILL_INFO.ISSUE_ID is '승인번호'
/

comment on column IF_TAX_BILL_INFO.ISSUE_DAY is '작성일자(YYYYMMDD형식): 매출기준일자'
/

comment on column IF_TAX_BILL_INFO.BILL_TYPE_CODE is '세금계산서종류'
/

comment on column IF_TAX_BILL_INFO.PURPOSE_CODE is '영수청구코드'
/

comment on column IF_TAX_BILL_INFO.AMENDMENT_CODE is '전자세금계산서 수정 사유코드'
/

comment on column IF_TAX_BILL_INFO.UPPER_MANAGE_ID is '부모관리번호 : 수정세금계산서 발행시 사용'
/

comment on column IF_TAX_BILL_INFO.DESCRIPTION is '비고'
/

comment on column IF_TAX_BILL_INFO.INVOICER_PARTY_ID is '공급자 사업자등록번호'
/

comment on column IF_TAX_BILL_INFO.INVOICER_TAX_REGIST_ID is '종사업장 식별코드'
/

comment on column IF_TAX_BILL_INFO.INVOICER_PARTY_NAME is '공급업체 사업체명'
/

comment on column IF_TAX_BILL_INFO.INVOICER_CEO_NAME is '공급업체 대표자명'
/

comment on column IF_TAX_BILL_INFO.INVOICER_ADDR is '공급업체의 주소'
/

comment on column IF_TAX_BILL_INFO.INVOICER_TYPE is '공급업체의 업태'
/

comment on column IF_TAX_BILL_INFO.INVOICER_CLASS is '공급업체의 업종'
/

comment on column IF_TAX_BILL_INFO.INVOICER_CONTACT_DEPART is '공급업체 담당부서'
/

comment on column IF_TAX_BILL_INFO.INVOICER_CONTACT_NAME is '공급업체 담당자명'
/

comment on column IF_TAX_BILL_INFO.INVOICER_CONTACT_PHONE is '공급업체 담당자 전화번호'
/

comment on column IF_TAX_BILL_INFO.INVOICER_CONTACT_EMAIL is '공급업체 담당자 이메일'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_BUSINESS_TYPE_CODE is '공급받는자 사업자등록번호 구분코드'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_PARTY_ID is '공급받는자 사업자등록번호'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_TAX_REGIST_ID is '공급받는자 종사업장 식별코드'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_PARTY_NAME is '공급받는자 사업체명'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_CEO_NAME is '공급받는자 대표자명'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_ADDR is '공급받는자 주소'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_TYPE is '공급받는자 업태'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_CLASS is '공급받는자 업종'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_CONTACT_DEPART1 is '공급받는자 담당부서'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_CONTACT_NAME1 is '공급받는자 담당자명'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_CONTACT_PHONE1 is '공급받는자 담당자 전화번호'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_CONTACT_EMAIL1 is '공급받는자 담당자 이메일'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_CONTACT_DEPART2 is '공급받는자 담당부서'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_CONTACT_NAME2 is '공급받는자 담당자명'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_CONTACT_PHONE2 is '공급받는자 담당자 전화번호'
/

comment on column IF_TAX_BILL_INFO.INVOICEE_CONTACT_EMAIL2 is '공급받는자 담당자 이메일'
/

comment on column IF_TAX_BILL_INFO.PAYMENT_TYPE_CODE1 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO.PAY_AMOUNT1 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO.PAYMENT_TYPE_CODE2 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO.PAY_AMOUNT2 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO.PAYMENT_TYPE_CODE3 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO.PAY_AMOUNT3 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO.PAYMENT_TYPE_CODE4 is '결제방법코드'
/

comment on column IF_TAX_BILL_INFO.PAY_AMOUNT4 is '결제방법별금액'
/

comment on column IF_TAX_BILL_INFO.CHARGE_TOTAL_AMOUNT is '총 공급가액 합계'
/

comment on column IF_TAX_BILL_INFO.TAX_TOTAL_AMOUNT is '총 세액 합계'
/

comment on column IF_TAX_BILL_INFO.GRAND_TOTAL_AMOUNT is '총액(공급가액+세액)'
/

comment on column IF_TAX_BILL_INFO.CUST_NO is '전기고객번호'
/

comment on column IF_TAX_BILL_INFO.BILL_YYYYMM is '청구년월'
/

comment on column IF_TAX_BILL_INFO.BILL_ISSUE_YYYYMM is '청구서발행년월'
/

comment on column IF_TAX_BILL_INFO.PAY_DEADLINE is '지급만기일자'
/

comment on column IF_TAX_BILL_INFO.ONLINE_GUB_CODE is '온,오프라인 구분코드(1:온라인,2:오프라인)'
/

comment on column IF_TAX_BILL_INFO.FLG is '처리구분(Y,N)'
/

comment on column IF_TAX_BILL_INFO.REGIST_DT is '등록일시'
/

comment on column IF_TAX_BILL_INFO.MODIFY_DT is '수정일시'
/

comment on column IF_TAX_BILL_INFO.CANCEL_DT is '취소일시'
/

comment on column IF_TAX_BILL_INFO.EAI_STAT is 'p:진행중 c:완료 e:에러'
/

comment on column IF_TAX_BILL_INFO.EAI_CDATE is 'EAI 연계 생성일시'
/

comment on column IF_TAX_BILL_INFO.EAI_UDATE is 'EAI 연계 갱신일시'
/

comment on column IF_TAX_BILL_INFO.GRP_NO is '배치분할차수'
/

create index IF_TAX_BILL_INFO_IX1
    on IF_TAX_BILL_INFO (ONLINE_GUB_CODE, FLG, ISSUE_DAY, MODIFY_DT)
    /

create index IF_TAX_BILL_INFO_IX2
    on IF_TAX_BILL_INFO (REL_SYSTEM_ID, BILL_ISSUE_YYYYMM)
    /

create unique index IF_TAX_BILL_INFO_PK
    on IF_TAX_BILL_INFO (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID)
    /

create unique index IF_TAX_BILL_INFO_UK
    on IF_TAX_BILL_INFO (ISSUE_ID)
    /

alter table IF_TAX_BILL_INFO
    add primary key (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID)
    /

create table IF_TAX_BILL_ITEM_LIST
(
    REL_SYSTEM_ID     VARCHAR2(10)  not null,
    JOB_GUB_CODE      VARCHAR2(6)   not null,
    MANAGE_ID         VARCHAR2(24)  not null,
    SEQ_NO            NUMBER(2)     not null,
    PURCHASE_DAY      VARCHAR2(8)   not null,
    ITEM_NAME         VARCHAR2(100) not null,
    ITEM_INFO         VARCHAR2(60),
    ITEM_DESC         VARCHAR2(100),
    UNIT_QUANTITY     NUMBER(10, 2) not null,
    UNIT_AMOUNT       NUMBER(16, 2) not null,
    INVOICE_AMOUNT    NUMBER(18)    not null,
    TAX_AMOUNT        NUMBER(18)    not null,
    BILL_ISSUE_YYYYMM VARCHAR2(6)
)
    /

comment on table IF_TAX_BILL_ITEM_LIST is '업무시스템연계 매출세금계산서 품목 정보'
/

comment on column IF_TAX_BILL_ITEM_LIST.REL_SYSTEM_ID is '연계시스템ID'
/

comment on column IF_TAX_BILL_ITEM_LIST.JOB_GUB_CODE is '업무구분코드(전력매출,공사매출 등)'
/

comment on column IF_TAX_BILL_ITEM_LIST.MANAGE_ID is '사업자관리번호'
/

comment on column IF_TAX_BILL_ITEM_LIST.SEQ_NO is '물품 일련번호'
/

comment on column IF_TAX_BILL_ITEM_LIST.PURCHASE_DAY is '물품 공급일자'
/

comment on column IF_TAX_BILL_ITEM_LIST.ITEM_NAME is '물품명'
/

comment on column IF_TAX_BILL_ITEM_LIST.ITEM_INFO is '물품에 대한 규격'
/

comment on column IF_TAX_BILL_ITEM_LIST.ITEM_DESC is '물품과 관련된 자유기술문'
/

comment on column IF_TAX_BILL_ITEM_LIST.UNIT_QUANTITY is '수량'
/

comment on column IF_TAX_BILL_ITEM_LIST.UNIT_AMOUNT is '물품 단가'
/

comment on column IF_TAX_BILL_ITEM_LIST.INVOICE_AMOUNT is '물품 공급 가액'
/

comment on column IF_TAX_BILL_ITEM_LIST.TAX_AMOUNT is '물품 세액'
/

comment on column IF_TAX_BILL_ITEM_LIST.BILL_ISSUE_YYYYMM is '청구발행년월'
/

create unique index IF_TAX_BILL_ITEM_LIST_PK
    on IF_TAX_BILL_ITEM_LIST (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID, SEQ_NO)
    /

alter table IF_TAX_BILL_ITEM_LIST
    add primary key (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID, SEQ_NO)
    /

create table TB_BATCH_JOB_HIST
(
    YYYYMM         VARCHAR2(6)   not null,
    BATCH_JOB_CODE VARCHAR2(2)   not null,
    SERVER_NO      VARCHAR2(2)   not null,
    START_DT       DATE          not null,
    END_DT         DATE          not null,
    DEST_CNT       NUMBER(10),
    WORK_CNT       NUMBER(10)    not null,
    DEST_AMT       NUMBER(15, 2) not null,
    WORK_AMT       NUMBER(15, 2) not null,
    WORK_STAT      VARCHAR2(1)   not null,
    REGIST_DT      DATE          not null,
    MODIFY_DT      DATE,
    GRP_NO         CHAR(2)
)
    /

comment on table TB_BATCH_JOB_HIST is '배치작업이력테이블'
/

comment on column TB_BATCH_JOB_HIST.YYYYMM is '대상년월'
/

comment on column TB_BATCH_JOB_HIST.BATCH_JOB_CODE is '배치작업코드'
/

comment on column TB_BATCH_JOB_HIST.SERVER_NO is '작업서버'
/

comment on column TB_BATCH_JOB_HIST.START_DT is '시작일시'
/

comment on column TB_BATCH_JOB_HIST.END_DT is '종료일시'
/

comment on column TB_BATCH_JOB_HIST.DEST_CNT is '대상건수'
/

comment on column TB_BATCH_JOB_HIST.WORK_CNT is '처리건수'
/

comment on column TB_BATCH_JOB_HIST.DEST_AMT is '대상금액'
/

comment on column TB_BATCH_JOB_HIST.WORK_AMT is '처리금액'
/

comment on column TB_BATCH_JOB_HIST.WORK_STAT is '처리상태(Sucess, Fail)'
/

comment on column TB_BATCH_JOB_HIST.REGIST_DT is '작성일'
/

comment on column TB_BATCH_JOB_HIST.MODIFY_DT is '최종수정일시'
/

comment on column TB_BATCH_JOB_HIST.GRP_NO is '배치분할차수'
/

create unique index TB_BATCH_JOB_HIST_PK
    on TB_BATCH_JOB_HIST (YYYYMM, SERVER_NO, BATCH_JOB_CODE, START_DT)
    /



create table TB_CERT_INFO
(
    OWNER             VARCHAR2(100) not null,
    BUSINESS_NO       VARCHAR2(13)  not null,
    REPOSITORY        VARCHAR2(100) not null,
    DN                VARCHAR2(200) not null,
    VALIDITY_START_DT VARCHAR2(16)  not null,
    VALIDITY_END_DT   VARCHAR2(16)  not null,
    PASS_WORD         VARCHAR2(250) not null,
    REGIST_DT         DATE          not null,
    MODIFY_DT         DATE,
    REGIST_ID         VARCHAR2(20)  not null,
    MODIFY_ID         VARCHAR2(20),
    USE_YN            VARCHAR2(1) default 'Y'
)
    /

comment on table TB_CERT_INFO is '전자서명 인증서 정보'
/

comment on column TB_CERT_INFO.OWNER is '사업자명'
/

comment on column TB_CERT_INFO.BUSINESS_NO is '사업자번호'
/

comment on column TB_CERT_INFO.REPOSITORY is '인증서경로'
/

comment on column TB_CERT_INFO.DN is '인증서 디엔'
/

comment on column TB_CERT_INFO.VALIDITY_START_DT is '발급일자'
/

comment on column TB_CERT_INFO.VALIDITY_END_DT is '유효기간'
/

comment on column TB_CERT_INFO.PASS_WORD is '비밀번호'
/

comment on column TB_CERT_INFO.REGIST_DT is '작성일'
/

comment on column TB_CERT_INFO.MODIFY_DT is '최종수정일시'
/

comment on column TB_CERT_INFO.REGIST_ID is '등록자ID'
/

comment on column TB_CERT_INFO.MODIFY_ID is '수정한 사람ID'
/

comment on column TB_CERT_INFO.USE_YN is '사용여부'
/

create unique index TB_CERT_INFO_PK
    on TB_CERT_INFO (OWNER, BUSINESS_NO, REPOSITORY)
    /

create index TB_CERT_INFO_IX1
    on TB_CERT_INFO (VALIDITY_END_DT)
    /

create index TB_CERT_INFO_IX2
    on TB_CERT_INFO (USE_YN)
    /

create table TB_BATCH_CONFIG
(
    SVR0_EXEC_YN      VARCHAR2(1)          not null,
    SVR1_EXEC_YN      VARCHAR2(1)          not null,
    SVR2_EXEC_YN      VARCHAR2(1)          not null,
    SVR3_EXEC_YN      VARCHAR2(1)          not null,
    THREAD_CNT        NUMBER(3)            not null,
    NEXT_EXEC_DT      DATE                 not null,
    XML_DEL_CODE      VARCHAR2(2)          not null,
    EXEC_CYCLE_MINUTE NUMBER(5)            not null,
    REGIST_DT         DATE default SYSDATE not null,
    MODIFY_DT         DATE default SYSDATE not null,
    NEXT_EXEC_DT1     DATE,
    NEXT_EXEC_DT2     DATE,
    NEXT_EXEC_DT3     DATE,
    GRP_CNT           CHAR(2),
    NOW_GRP_NO        CHAR(2)
)
    /

comment on table TB_BATCH_CONFIG is '배치작업환경'
/

comment on column TB_BATCH_CONFIG.SVR0_EXEC_YN is '0번서버'
/

comment on column TB_BATCH_CONFIG.SVR1_EXEC_YN is '1번서버'
/

comment on column TB_BATCH_CONFIG.SVR2_EXEC_YN is '2번서버'
/

comment on column TB_BATCH_CONFIG.SVR3_EXEC_YN is '3번서버'
/

comment on column TB_BATCH_CONFIG.THREAD_CNT is '쓰레드 수'
/

comment on column TB_BATCH_CONFIG.NEXT_EXEC_DT is '다음 수행 시간'
/

comment on column TB_BATCH_CONFIG.XML_DEL_CODE is '파일 삭제 조건'
/

comment on column TB_BATCH_CONFIG.EXEC_CYCLE_MINUTE is '온라인 배치 수행 주기'
/

comment on column TB_BATCH_CONFIG.REGIST_DT is '작성일'
/

comment on column TB_BATCH_CONFIG.MODIFY_DT is '수정일'
/

comment on column TB_BATCH_CONFIG.NEXT_EXEC_DT1 is '다음실행일1'
/

comment on column TB_BATCH_CONFIG.NEXT_EXEC_DT2 is '다음실행일2'
/

comment on column TB_BATCH_CONFIG.NEXT_EXEC_DT3 is '다음실행일3'
/

comment on column TB_BATCH_CONFIG.GRP_CNT is '배치분할갯수'
/

comment on column TB_BATCH_CONFIG.NOW_GRP_NO is '현재 처리중인 배치분할 차수'
/

