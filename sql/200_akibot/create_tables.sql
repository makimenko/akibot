/*
    drop table t_log;
*/

CREATE SEQUENCE S_LOG INCREMENT BY 1 CACHE 1000;

CREATE TABLE T_LOG (
    ID          NUMBER(20),
    LOG_DATE    TIMESTAMP(6),
    LOG_LEVEL   VARCHAR2(10),
    LOGGER      VARCHAR2(100),
    MESSAGE     CLOB,
    COMPONENT_NAME VARCHAR2(100),
    THROWABLE   CLOB

);


alter table t_log add constraint pk_log primary key (id);

create or replace view v_log as
select 
      l.id
    , l.log_date
    , l.log_level
    , l.logger
    , to_char(regexp_substr(message, '^\[(.*?)\]', 1, 1, null, 1)) client_name
    , to_char(substr(message,1,4000)) message
    , l.throwable
from t_log l 
;


create or replace view v_log_msg as
select l.id
    , l.log_date
    , l.client_name
    , to_char(case 
        when l.logger like '%IncommingMessageReceiver' then 'IN'
        when l.logger like '%OutgoingMessageManager%' then 'OUT'
        else l.logger
      end) direction
    , substr(message, instr(message,'/', 1, 1)+2, instr(message,'/', 1, 2)-instr(message,'/', 1, 1)-3) msg_from
    , substr(message, instr(message,'/', 1, 2)+2, instr(message,'/', 1, 3)-instr(message,'/', 1, 2)-3) msg_to
    , substr(message, instr(message,'/', 1, 3)+2) msg
from v_log l
where REGEXP_LIKE(message, 'MSG / .+ / .+ / .+')
order by l.id desc
;


