package io.coolexplorer.auth.filter;

import io.coolexplorer.auth.consts.PagingConst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class AccountSearchFilter {
    @Setter
    private String searchField;

    @Setter
    private String searchQuery;

    @Setter
    private String orderBy;

    @Setter
    private String order;

    @Setter
    private String role;

    @Setter
    private Long roleId;

    private int page;

    private int itemsPerPage;

    public AccountSearchFilter setPage(Integer page) {
        this.page = page != null && page > 0 ? page - 1 : PagingConst.DEFAULT_PAGE_NUM;
        return this;
    }

    public AccountSearchFilter setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage != null && itemsPerPage > 0 ? itemsPerPage : PagingConst.DEFAULT_ITEMS_PER_PAGE;
        return this;
    }
}
