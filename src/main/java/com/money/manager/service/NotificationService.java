package com.money.manager.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.money.manager.dto.ExpenseDto;
import com.money.manager.entity.ProfileEntity;
import com.money.manager.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

    //@Scheduled(cron = "0 * * * * *", zone = "IST")
    @Scheduled(cron="0 0 22 * * *",zone = "IST")
    public void sendDailyIncomeExpenseRemainder() {
        log.info("Job started : sendDailyIncomeExpenseRemainder");
        List<ProfileEntity> profiles = profileRepository.findAll();

        for (ProfileEntity profile : profiles) {
        	String body = "<html><body>"
        	        + "Hi " + profile.getFullName() + ",<br><br>"
        	        + "This is a friendly reminder to add your income and expense for today in Money Manager,<br><br>"
        	        + "<table cellpadding='0' cellspacing='0' border='0'><tr><td>"
        	        + "<a href='" + frontendUrl + "' style='display:inline-block;padding:10px 20px;"
        	        + "background-color:#4CAF50;color:#fff;text-decoration:none;"
        	        + "border-radius:5px;font-weight:bold;'>Go to Money Manager</a>"
        	        + "</td></tr></table>"
        	        + "<br><br>Best regards,<br>Money Manager Team"
        	        + "</body></html>";
                    

            emailService.sendMail(profile.getEmail(), "Daily Reminder: Add your income and expenses", body);
        }
        log.info("Job finished : sendDailyIncomeExpenseReminder");
        
    }
    
    //@Scheduled(cron = "0 * * * * *", zone = "IST")
    @Scheduled(cron = "0 0 23 * * *",zone="IST")
    public void sendDailyExpensesSummary() {
    	log.error("Job started : sendDailyExpensesSummary");
    	
    	List<ProfileEntity> profiles=profileRepository.findAll();
    	log.info("Number of profiles:"+ profiles.size());
    	for(ProfileEntity profile:profiles) {
    		List<ExpenseDto> todayExpenses=expenseService.getExpensesUserOnDate(profile.getId(), LocalDate.now());
    		log.error("number of expenses"+todayExpenses.size());
    		if(!todayExpenses.isEmpty()) {
    			StringBuilder table=new StringBuilder();
    			table.append("<table style='border-collapse:collapse;width:100%;'>");
    			table.append("<tr style='background-color:#f2f2f2;'><th style='border:1px solid #ddd;padding:8px;'>S.No</th><th style='border:1px solid #ddd;padding:8px;'>Name</th><th style='border:1px solid #ddd;padding:8px;'>Amount</th><th style='border:1px solid #ddd;padding:8px;'>Category</th>");
    			int i=1;
    			for(ExpenseDto expense:todayExpenses) {
    				table.append("<tr>");
    				table.append("<td style='border:1px solid #ddd;padding:8px; '>").append(i++).append("</td>");
    				table.append("<td style='border:1px solid #ddd;padding:8px; '>").append(expense.getName()).append("</td>");
    				table.append("<td style='border:1px solid #ddd;padding:8px; '>").append(expense.getAmount()).append("</td>");
    				table.append("<td style='border:1px solid #ddd;padding:8px; '>").append(expense.getCategoryId()!=null?expense.getCategoryName():"N/A").append("</td>");
    				table.append("</tr>");
    			}
    			table.append("</table>");
    			String body="Hi "+profile.getFullName()+",<br><br> Here is a summary of your expenses for today: <br/><br/>"+table+"<br/><br/>Best Regards,<br/> Money Manager Team";
    			emailService.sendMail(profile.getEmail(), "Your daily Expense summary", body);
    		}
    		
    	}
    	log.info("Job completed sendDailyExpensesSummary ");
    }
    
    
}
