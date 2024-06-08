package ltd.jellyfish.modules.items.services;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import ltd.jellyfish.models.Result;
import ltd.jellyfish.modules.items.data.ItemsDataRepository;
import ltd.jellyfish.modules.items.data.PricesDataRepository;
import ltd.jellyfish.modules.items.models.ItemPriceDTO;
import ltd.jellyfish.modules.items.models.Items;
import ltd.jellyfish.modules.items.models.Prices;

@Service
public record ItemListService(
    ItemsDataRepository itemsDataRepository,
    PricesDataRepository pricesDataRepository
) {

    public Result<List<ItemPriceDTO>> getItemLists() {
        List<Items> items = itemsDataRepository.findAll();
        List<ItemPriceDTO> itemPriceDTOs = new ArrayList<>();
        for (Items sigItem : items) {
            Prices prices = new Prices();
            prices.setItemId(sigItem.getId());
            List<Prices> pricesList = new ArrayList<>();
            Example<Prices> pricesExample = Example.of(prices);
            pricesList = pricesDataRepository.findAll(pricesExample);
            Prices alRollPrices = null;
            for (Prices sigPrices : pricesList) {
                if (alRollPrices == null) {
                    alRollPrices = sigPrices;
                } else {
                    if (alRollPrices.getUpdateTime().before(sigPrices.getUpdateTime())) {
                        alRollPrices = sigPrices;
                    }
                }
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
            String newestUpdateTime = simpleDateFormat.format(alRollPrices.getUpdateTime());
            ItemPriceDTO itemPriceDTO = new ItemPriceDTO();
            itemPriceDTO.setItemName(sigItem.getItemName());
            itemPriceDTO.setItemId(sigItem.getId());
            itemPriceDTO.setItemCreateTime(simpleDateFormat.format(sigItem.getCreateTime()));
            itemPriceDTO.setUpdateTime(newestUpdateTime);
            itemPriceDTO.setItemNewestPrice(alRollPrices.getPrice());
            itemPriceDTOs.add(itemPriceDTO);
        }
        Result<List<ItemPriceDTO>> reply = new Result<>();
        reply.setSuccess(true);
        reply.setData(itemPriceDTOs);
        return reply;
    }

    public Result<List<Prices>> priceHistory(String itemId) {
        Result<List<Prices>> reply = new Result<>();
        List<Prices> pricesList = new ArrayList<>();
        Prices prices = new Prices();
        prices.setItemId(itemId);
        Example<Prices> pricesExample = Example.of(prices);
        pricesList = pricesDataRepository.findAll(pricesExample);
        reply.setData(pricesList);
        reply.setSuccess(true);
        return reply;
    }
}
