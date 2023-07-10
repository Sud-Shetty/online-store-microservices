DELETE FROM t_order_order_line_items_list;
DELETE FROM t_ORDER;
DELETE FROM t_order_line_items;

INSERT INTO t_order (id, order_number) values (1,'1ehs2jkf34bjrf34nf3n6');
INSERT INTO t_order_line_items(id, sku_code, price, quantity) values (1, 'pixel7', 1200.00, 1);
INSERT INTO t_order_order_line_items_list(order_id, order_line_items_list_id) values (1, 1);

