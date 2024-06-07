package ltd.jellyfish.modules.items.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ltd.jellyfish.models.Result;
import ltd.jellyfish.modules.items.models.ItemPriceDTO;
import ltd.jellyfish.modules.items.services.ItemListService;

@RestController
public record ItemsListController(
    ItemListService itemListService
) {

    @GetMapping("/item/list")
    public Result<List<ItemPriceDTO>> itemsList(){
        return itemListService.getItemLists();
    }
}
