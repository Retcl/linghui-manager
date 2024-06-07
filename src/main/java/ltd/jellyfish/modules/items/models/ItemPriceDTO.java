package ltd.jellyfish.modules.items.models;

public class ItemPriceDTO {

    private String itemId;

    private String itemName;

    private String itemCreateTime;

    private String itemNewestPrice;

    private String updateTime;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCreateTime() {
        return itemCreateTime;
    }

    public void setItemCreateTime(String itemCreateTime) {
        this.itemCreateTime = itemCreateTime;
    }

    public String getItemNewestPrice() {
        return itemNewestPrice;
    }

    public void setItemNewestPrice(String itemNewestPrice) {
        this.itemNewestPrice = itemNewestPrice;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    
}
