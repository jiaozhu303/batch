package net.ldauvilaire.sample.batch.job.second;

import net.ldauvilaire.sample.batch.job.JobConstants;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(JobConstants.SECOND_JOB_ITEM_WRITER_ID)
public class SecondItemWriter<T>  implements ItemWriter<T>, InitializingBean {

    private ValueOperations<String, Object> redisTemplate;


    public SecondItemWriter(ValueOperations<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void write(List<? extends T> list) throws Exception {
        Map<String, Object> redisData = new HashMap<>();
        for (T item: list){
            redisData.putAll((Map<? extends String, ? extends Object>) item);
        }
        redisTemplate.multiSet(redisData);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
