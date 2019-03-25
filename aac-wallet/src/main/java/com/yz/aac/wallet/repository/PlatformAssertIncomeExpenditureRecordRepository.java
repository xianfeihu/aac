package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.repository.domain.PlatformAssertIncomeExpenditureRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface PlatformAssertIncomeExpenditureRecordRepository {

	 String SAVE_INCOME_EXPENDITURE_RECORD = "INSERT INTO platform_assert_income_expenditure_record(direction, user_id, user_name, "
	 		+ "action_time, amount, rmb_amount, action) "
	    		+ "values(#{direction}, #{userId}, #{userName}, #{actionTime}, #{amount}, #{rmbAmount}, "
	    		+ "#{action})";
	    
	    @Insert(SAVE_INCOME_EXPENDITURE_RECORD)
	    @Options(useGeneratedKeys = true, keyProperty = "id")
	    int saveIncomeExpenditureRecord(PlatformAssertIncomeExpenditureRecord platformAssertIncomeExpenditureRecord);
}
