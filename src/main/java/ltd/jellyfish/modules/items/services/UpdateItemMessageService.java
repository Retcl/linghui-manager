package ltd.jellyfish.modules.items.services;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import ltd.jellyfish.models.Result;
import ltd.jellyfish.modules.items.data.ItemsDataRepository;
import ltd.jellyfish.modules.items.data.PricesDataRepository;
import ltd.jellyfish.modules.items.models.Items;
import ltd.jellyfish.modules.items.models.Prices;

@Service
public record UpdateItemMessageService(
    ItemsDataRepository itemsDataRepository,
    PricesDataRepository pricesDataRepository
) {

    /**
     * 更改物品名称
     * @param itemId 物品ID
     * @param newName 物品的新名称
     * @return 统一返回标识
     */
    public Result<?> modifyItemName(String itemId, String newName) {
        Items items = new Items();
        items.setId(itemId);
        Example<Items> itemsExample = Example.of(items);
        items = itemsDataRepository.findOne(itemsExample).get();
        items.setItemName(newName);
        itemsDataRepository.save(items);
        return new Result<>(true, "", null);
    }

    /**
     * 删除物品和物品相关的价格历史
     * @param itemId 物品的唯一标识ID
     * @return 统一返回数据格式
     */
    public Result<?> deleteItem(String itemId) {
        itemsDataRepository.deleteById(itemId);
        Prices prices = new Prices();
        prices.setItemId(itemId);
        Example<Prices> pricesExample = Example.of(prices);
        List<Prices> pricesList = pricesDataRepository.findAll(pricesExample);
        pricesDataRepository.deleteAllInBatch(pricesList);
        return new Result<>(true, "", null);
    }

    /**
     * 更新价格
     * @param itemId 物品ID
     * @param newPrice 新价格
     * @return 统一返回数据格式
     */
    public Result<?> updateItemPrice(String itemId, String newPrice) {
        Items items = new Items();
        items.setId(itemId);
        Example<Items> itemsExample = Example.of(items);
        items = itemsDataRepository.findOne(itemsExample).get();
        items.setUpdateTime(new Date(System.currentTimeMillis()));
        itemsDataRepository.save(items);
        Prices prices = new Prices();
        prices.setItemId(itemId);
        prices.setPrice(newPrice);
        prices.setUpdateTime(new Date(System.currentTimeMillis()));
        pricesDataRepository.save(prices);
        return new Result<>(true, "", null);
    }
}
