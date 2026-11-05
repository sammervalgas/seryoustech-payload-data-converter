-- Carga inicial de exemplo para o banco H2
INSERT INTO tb_products (name, description, price, stock_quantity, active, created_at, updated_at)
VALUES ('Teclado Mecânico RGB', 'Teclado mecânico switch brown padrão ABNT2', 349.90, 25, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tb_products (name, description, price, stock_quantity, active, created_at, updated_at)
VALUES ('Mouse Gamer 16000 DPI', 'Mouse óptico ergonômico com 6 botões programáveis', 189.50, 40, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tb_products (name, description, price, stock_quantity, active, created_at, updated_at)
VALUES ('Monitor Ultrawide 29"', 'Monitor IPS 75Hz Full HD com FreeSync', 1299.00, 10, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tb_products (name, description, price, stock_quantity, active, created_at, updated_at)
VALUES ('Headset Gamer 7.1', 'Headset com cancelamento passivo de ruído e microfone removível', 279.99, 15, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
