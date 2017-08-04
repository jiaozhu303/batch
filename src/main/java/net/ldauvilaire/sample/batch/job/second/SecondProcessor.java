package net.ldauvilaire.sample.batch.job.second;

import lombok.extern.slf4j.Slf4j;
import net.ldauvilaire.sample.batch.domain.model.CCVVIsibility;
import net.ldauvilaire.sample.batch.job.JobConstants;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component(JobConstants.SECOND_JOB_ITEM_PROCESSOR_ID)
public class SecondProcessor implements ItemProcessor<CCVVIsibility, Map> {


    @Override
    public Map<String, Object> process(CCVVIsibility item) throws Exception {
//        log.info("record id:{}", item.getId());
        Map<String, Object> writerSo = new HashMap<>();
        writerSo.put(item.getId(), item);
        return writerSo;
    }
}