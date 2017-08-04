package net.ldauvilaire.sample.batch.job;

import net.ldauvilaire.sample.batch.domain.model.CCVVIsibility;
import net.ldauvilaire.sample.batch.job.second.SecondItemWriter;
import net.ldauvilaire.sample.batch.job.second.SecondListener;
import net.ldauvilaire.sample.batch.job.second.SecondProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.Map;

@Configuration
public class SecondJobConfiguration {

	@Value("${first.chunck.size}")
	protected Integer chunckSize;

	@Autowired
	protected JobBuilderFactory jobBuilderFactory;

	@Autowired
	protected StepBuilderFactory stepBuilderFactory;

	@Autowired
	protected JobRepository jobRepository;


	@Resource(name=JobConstants.SECOND_JOB_ITEM_PROCESSOR_ID)
	protected SecondProcessor processor;

	@Resource(name=JobConstants.SECOND_JOB_ITEM_WRITER_ID)
	protected SecondItemWriter<Map> writer;

	@Resource(name=JobConstants.SECOND_JOB_EXECUTION_LISTENER_ID)
	protected SecondListener listener;

	@Bean
	public Job secondJob(@Qualifier("secondStep") Step secondStep) throws Exception {
		return jobBuilderFactory.get(JobConstants.SECOND_JOB_ID)
				.repository(jobRepository)
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(secondStep)
				.end()
				.build();
	}

	@Bean
	public Step secondStep(DataSourceTransactionManager transactionManager, ItemReader<CCVVIsibility> secondReader) throws Exception {
		return stepBuilderFactory.get(JobConstants.SECOND_JOB_STEP_ID)
				.repository(jobRepository)
				.<CCVVIsibility, Map> chunk(chunckSize)
				.reader(secondReader)
				.processor(processor)
				.writer(writer)
                .taskExecutor(new SimpleAsyncTaskExecutor("LSC-"))
                .throttleLimit(3)
                .transactionManager(transactionManager)
				.build();
	}

	@Bean
	public ItemReader<CCVVIsibility> secondReader(DataSource dataSource) {


		// mssql 分页查询
//        JdbcPagingItemReader<CCVVIsibility> reader = new JdbcPagingItemReader<>();
//        SqlServerPagingQueryProvider queryProvider = new SqlServerPagingQueryProvider();
//
//        queryProvider.setSelectClause("SELECT ID, MATERIAL, LAND, CHAR, CHARVALUE, EOW, ANNOUNCE, WITHDRAW, ACTIVE, INTERNAL_AUDIENC");
//        queryProvider.setFromClause("FROM C_CV_VISIBILITY");
//        Map<String, Order> keys = new HashMap<>();
//        keys.put("ID", Order.DESCENDING);
//        queryProvider.setSortKeys(keys);
//        reader.setQueryProvider(queryProvider);
//        reader.setPageSize(1000);
//        reader.setDataSource(dataSource);
		JdbcCursorItemReader<CCVVIsibility> reader = new JdbcCursorItemReader<>();
		reader.setSql("SELECT ID, MATERIAL, LAND, CHAR, CHARVALUE, EOW, ANNOUNCE, WITHDRAW, ACTIVE, INTERNAL_AUDIENC FROM C_CV_VISIBILITY");
		reader.setDataSource(dataSource);
		reader.setRowMapper(
				(ResultSet resultSet, int rowNum) -> {
					CCVVIsibility recordSO = new CCVVIsibility();
					recordSO.setActive(resultSet.getString("ACTIVE"));
					recordSO.setAnnounce(resultSet.getDate("ANNOUNCE"));
					recordSO.setCharName(resultSet.getString("CHAR"));
					recordSO.setCharValue(resultSet.getString("CHARVALUE"));
					recordSO.setId(resultSet.getString("ID"));
					recordSO.setInternalAudienc(resultSet.getString("INTERNAL_AUDIENC"));
					recordSO.setLand(resultSet.getString("LAND"));
					recordSO.setMaterial(resultSet.getString("MATERIAL"));
					recordSO.setEow(resultSet.getDate("EOW"));
					recordSO.setWithdraw(resultSet.getDate("WITHDRAW"));

					return recordSO;
				});
		return reader;
	}
}
