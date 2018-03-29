CREATE TABLE T_STOCK_CATEGORY(
	 	STOCK_CATEGORY_ID varchar(10) not null,
        MARKET_TYPE varchar(10) not null,
        CATEGORY_NAME varchar(100),
        ORDER_NO int4,
        primary key (STOCK_CATEGORY_ID)
);

CREATE TABLE T_STOCK(
        STOCK_ID varchar(10) not null,
        STOCK_NAME varchar(255) ,
        OVERSEAS_TYPE int2  null,
        STOCK_CATEGORY_ID varchar(10) not null,
        primary key (STOCK_ID),
        FOREIGN KEY (STOCK_CATEGORY_ID) REFERENCES T_STOCK_CATEGORY(STOCK_CATEGORY_ID)
);

create table T_STOCK_SHEET (
        STOCK_ID varchar(10) not null,
        CAL_YEAR date not null,
        EPS_Q1 numeric(19, 2),
        EPS_Q2 numeric(19, 2),
        EPS_Q3 numeric(19, 2),
        EPS_Q4 numeric(19, 2),
        CASH_DIVIDEND numeric(19, 2),
        STOCK_DIVIDEND numeric(19, 2),
        NET_INCOME numeric(19, 2),
        ROE_RATE numeric(19, 2),
        INSERT_TIME TIMESTAMP  DEFAULT now(),
        UPDATE_TIME TIMESTAMP ,
        primary key (STOCK_ID,CAL_YEAR)
    );