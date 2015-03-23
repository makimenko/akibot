alter session set nls_timestamp_format = 'yyyy.mm.dd hh24:mi:ss.ff'


delete from t_log;
commit;


select *
from v_log l 
where 1=1
    and l.id > 46409
    and l.client_name like 'akibot.test'
--    and logger = 'com.akibot.engine2.network.ClientDescriptionUtils'
order by id desc;



select *
from v_log_msg
where 1=1
--    and direction = 'OUT'
    and id > 46409
order by 2 desc


