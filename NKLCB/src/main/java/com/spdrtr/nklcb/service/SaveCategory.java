package com.spdrtr.nklcb.service;

import com.spdrtr.nklcb.domain.Category;
import com.spdrtr.nklcb.repository.CategoryRepository;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.spdrtr.nklcb.service.Crawling.*;

@Service
@Transactional
public class SaveCategory {
    private final CategoryRepository categoryRepository;

    @Autowired
    public SaveCategory(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void saveCategory() throws InterruptedException {
        String url = "https://www.wanted.co.kr/wdlist?country=kr&job_sort=job.popularity_order&years=-1&locations=all";
        process(url);
        String category_depth1;
        List<String> categories_depth2 = new ArrayList<>();
        try {
            findButton("JobGroup_JobGroup__H1m1m").click();
            final int Big_category_count = findButtons("JobGroupItem_JobGroupItem__xXzAi").size();
            for(int i=0; i< Big_category_count; i++){
                WebElement Big_category = findButtons("JobGroupItem_JobGroupItem__xXzAi").get(i);
                category_depth1 = Big_category.getText();
                System.out.println("상위카테고리:" + category_depth1);
                Big_category.click();

                findButton("JobCategory_JobCategory__btn__k3EFe").click();
                categories_depth2 = getData("JobCategoryItem_JobCategoryItem__oUaZr");
                for(String category_depth2 : categories_depth2){
                    Category category = Category.of(category_depth1, category_depth2);
                    categoryRepository.save(category);
                    System.out.println(category_depth2);
                }
                findButton("JobGroup_JobGroup__H1m1m").click();
                System.out.println("-------------------------------");
            }
        } catch (Exception o) {
            System.out.println("err occur");
        }
//        driver.quit();
    }
}
