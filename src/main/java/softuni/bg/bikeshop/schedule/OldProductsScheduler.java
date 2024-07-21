package softuni.bg.bikeshop.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import softuni.bg.bikeshop.service.ProductsService;

@Component
public class OldProductsScheduler {
    private final ProductsService productsService;

    public OldProductsScheduler(ProductsService productsService) {
        this.productsService = productsService;
    }
    @Scheduled(cron = "0 0 3 * * ?")
    public void detectOldProducts(){
        if(this.productsService.getProductsCount() > 0){
            this.productsService.deleteOldProducts();
        }
    }
}
