delete from t_log;
commit;

select 
      l.id
    , l.log_date
    , l.log_level
    , l.logger
    , to_char(message) message
    , l.throwable
from t_log l 
order by 1 desc;



select *
from v_log_msg
where 1=1
  and direction = 'IN'
    and id > 3640



