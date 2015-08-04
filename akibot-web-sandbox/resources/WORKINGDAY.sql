create sequence  hibernate_sequence;
create sequence S_WORKINGDAY;

CREATE TABLE T_WORKINGDAY (
  ID NUMBER(20) not null,
  DAY DATE,
  BEGINTIME DATE,
  ENDTIME DATE,
  HOURS NUMBER NOT NULL,
  DESCRIPTION VARCHAR2(400) NOT NULL
);

ALTER TABLE T_WORKINGDAY ADD CONSTRAINT PK_WORKINGDAY PRIMARY KEY (ID);

create trigger Trg_WORKINGDAY
   before insert on T_WORKINGDAY
   for each row
 begin
   :new.ID := S_WORKINGDAY.nextval;
 end;
/

--------------
insert into T_WORKINGDAY (HOURS, DESCRIPTION) values (1,'a');
commit;

select * from T_WORKINGDAY;

