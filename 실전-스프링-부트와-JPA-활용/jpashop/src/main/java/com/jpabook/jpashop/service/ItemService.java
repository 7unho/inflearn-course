package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    @Transactional
    public void updateItem(Long itemId, String bookName, int bookPrice, int bookStockQuantity) {
        Item findItem = itemRepository.findOne(itemId); // find로 찾아온 item은 em이 관리중인 상태

        /*
            변경 감지로 인해 다시 save할 필요가 없다.
         */
        findItem.updateItem(bookName, bookPrice, bookStockQuantity);
//        findItem.setName(bookName);
//        findItem.setPrice(bookPrice);
//        findItem.setStockQuantity(bookStockQuantity);
//        -> setter보다는 별개의 메서드로 분리해서 데이터 변경점 추적이 용이하게
    }
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }
}