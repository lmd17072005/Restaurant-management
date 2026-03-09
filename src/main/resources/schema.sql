-- ============================================================
-- HE THONG QUAN LY NHA HANG - PHIEN BAN POSTGRESQL
-- Chuyen doi tu SQL Server sang PostgreSQL
-- ============================================================

-- ============================================================
-- EXTENSIONS CAN THIET
-- ============================================================
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ============================================================
-- DROP TABLES (theo thu tu phu thuoc)
-- ============================================================
DROP TABLE IF EXISTS audit_log CASCADE;
DROP TABLE IF EXISTS thanh_toan CASCADE;
DROP TABLE IF EXISTS chi_tiet_don_hang CASCADE;
DROP TABLE IF EXISTS don_hang CASCADE;
DROP TABLE IF EXISTS hoa_don CASCADE;
DROP TABLE IF EXISTS dat_ban CASCADE;
DROP TABLE IF EXISTS lich_su_kho CASCADE;
DROP TABLE IF EXISTS cong_thuc_mon CASCADE;
DROP TABLE IF EXISTS nguyen_lieu CASCADE;
DROP TABLE IF EXISTS mon_an CASCADE;
DROP TABLE IF EXISTS danh_muc_mon CASCADE;
DROP TABLE IF EXISTS ban_an CASCADE;
DROP TABLE IF EXISTS nguoi_dung CASCADE;

-- ============================================================
-- DROP TYPES (neu co)
-- ============================================================
DROP TYPE IF EXISTS vai_tro_enum CASCADE;
DROP TYPE IF EXISTS trang_thai_nguoi_dung_enum CASCADE;
DROP TYPE IF EXISTS trang_thai_ban_enum CASCADE;
DROP TYPE IF EXISTS trang_thai_danh_muc_enum CASCADE;
DROP TYPE IF EXISTS trang_thai_mon_enum CASCADE;
DROP TYPE IF EXISTS trang_thai_nguyen_lieu_enum CASCADE;
DROP TYPE IF EXISTS loai_giao_dich_kho_enum CASCADE;
DROP TYPE IF EXISTS trang_thai_dat_ban_enum CASCADE;
DROP TYPE IF EXISTS trang_thai_hoa_don_enum CASCADE;
DROP TYPE IF EXISTS trang_thai_don_hang_enum CASCADE;
DROP TYPE IF EXISTS trang_thai_chi_tiet_enum CASCADE;
DROP TYPE IF EXISTS phuong_thuc_thanh_toan_enum CASCADE;
DROP TYPE IF EXISTS audit_action_enum CASCADE;

-- ============================================================
-- CUSTOM ENUM TYPES (thay cho CHECK IN (...))
-- ============================================================
CREATE TYPE vai_tro_enum AS ENUM ('QUAN_LY', 'NHAN_VIEN', 'KHACH_HANG');
CREATE TYPE trang_thai_nguoi_dung_enum AS ENUM ('hoat_dong', 'ngung_hoat_dong');
CREATE TYPE trang_thai_ban_enum AS ENUM ('trong', 'da_dat', 'dang_phuc_vu', 'bao_tri');
CREATE TYPE trang_thai_danh_muc_enum AS ENUM ('hoat_dong', 'an');
CREATE TYPE trang_thai_mon_enum AS ENUM ('con_ban', 'het_mon', 'ngung_ban');
CREATE TYPE trang_thai_nguyen_lieu_enum AS ENUM ('hoat_dong', 'ngung_su_dung');
CREATE TYPE loai_giao_dich_kho_enum AS ENUM ('nhap', 'xuat', 'dieu_chinh');
CREATE TYPE trang_thai_dat_ban_enum AS ENUM ('cho_xac_nhan', 'da_xac_nhan', 'da_huy', 'khach_den', 'khach_khong_den');
CREATE TYPE trang_thai_hoa_don_enum AS ENUM ('chua_thanh_toan', 'thanh_toan_mot_phan', 'da_thanh_toan', 'da_huy');
CREATE TYPE trang_thai_don_hang_enum AS ENUM ('cho_che_bien', 'dang_che_bien', 'san_sang', 'da_phuc_vu', 'huy');
CREATE TYPE trang_thai_chi_tiet_enum AS ENUM ('cho_che_bien', 'dang_che_bien', 'hoan_thanh', 'huy');
CREATE TYPE phuong_thuc_thanh_toan_enum AS ENUM ('tien_mat', 'the', 'chuyen_khoan', 'vi_dien_tu');
CREATE TYPE audit_action_enum AS ENUM ('INSERT', 'UPDATE', 'DELETE');

-- ============================================================
-- 1) NGUOI_DUNG (Tat ca nguoi dung: Quan ly, Nhan vien, Khach hang)
-- ============================================================
CREATE TABLE nguoi_dung (
    nguoi_dung_id   BIGSERIAL PRIMARY KEY,
    vai_tro         vai_tro_enum NOT NULL,
    ho_ten          VARCHAR(100) NOT NULL,
    so_dien_thoai   VARCHAR(20) NULL,
    email           VARCHAR(100) NULL,
    ten_dang_nhap   VARCHAR(50) NOT NULL,
    mat_khau_hash   VARCHAR(255) NOT NULL,
    trang_thai      trang_thai_nguoi_dung_enum NOT NULL DEFAULT 'hoat_dong',
    ngay_tao        TIMESTAMP(0) NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX uq_nd_sdt ON nguoi_dung(so_dien_thoai) WHERE so_dien_thoai IS NOT NULL;
CREATE UNIQUE INDEX uq_nd_email ON nguoi_dung(email) WHERE email IS NOT NULL;
CREATE UNIQUE INDEX uq_nd_username ON nguoi_dung(ten_dang_nhap);
CREATE INDEX idx_nd_vai_tro ON nguoi_dung(vai_tro);

-- ============================================================
-- 2) BAN_AN
-- ============================================================
CREATE TABLE ban_an (
    ban_id      SERIAL PRIMARY KEY,
    ma_ban      VARCHAR(10) NOT NULL UNIQUE,
    suc_chua    INT NOT NULL,
    trang_thai  trang_thai_ban_enum NOT NULL DEFAULT 'trong',

    CONSTRAINT ck_ba_suc_chua CHECK (suc_chua > 0)
);

CREATE INDEX idx_ba_trang_thai ON ban_an(trang_thai);

-- ============================================================
-- 3) DANH_MUC_MON
-- ============================================================
CREATE TABLE danh_muc_mon (
    danh_muc_id SERIAL PRIMARY KEY,
    ten_danh_muc VARCHAR(100) NOT NULL UNIQUE,
    trang_thai  trang_thai_danh_muc_enum NOT NULL DEFAULT 'hoat_dong'
);

-- ============================================================
-- 4) MON_AN
-- ============================================================
CREATE TABLE mon_an (
    mon_id      SERIAL PRIMARY KEY,
    danh_muc_id INT NOT NULL,
    ten_mon     VARCHAR(150) NOT NULL,
    gia         DECIMAL(12,2) NOT NULL,
    hinh_anh    VARCHAR(500) NULL,
    trang_thai  trang_thai_mon_enum NOT NULL DEFAULT 'con_ban',
    ngay_tao    TIMESTAMP(0) NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_ma_dm FOREIGN KEY (danh_muc_id) REFERENCES danh_muc_mon(danh_muc_id),
    CONSTRAINT ck_ma_gia CHECK (gia >= 0)
);

CREATE INDEX idx_ma_dm ON mon_an(danh_muc_id);
CREATE INDEX idx_ma_ten ON mon_an(ten_mon);
CREATE INDEX idx_ma_trang_thai ON mon_an(trang_thai);

-- ============================================================
-- 5) NGUYEN_LIEU (ton kho)
-- ============================================================
CREATE TABLE nguyen_lieu (
    nguyen_lieu_id  SERIAL PRIMARY KEY,
    ten_nguyen_lieu VARCHAR(150) NOT NULL UNIQUE,
    don_vi_tinh     VARCHAR(30) NOT NULL,
    ton_kho         DECIMAL(12,3) NOT NULL DEFAULT 0,
    ton_kho_toi_thieu DECIMAL(12,3) NOT NULL DEFAULT 0,
    trang_thai      trang_thai_nguyen_lieu_enum NOT NULL DEFAULT 'hoat_dong',
    ngay_tao        TIMESTAMP(0) NOT NULL DEFAULT NOW(),

    CONSTRAINT ck_nl_ton CHECK (ton_kho >= 0 AND ton_kho_toi_thieu >= 0)
);

CREATE INDEX idx_nl_ton_kho ON nguyen_lieu(ton_kho, ton_kho_toi_thieu);

-- ============================================================
-- 6) CONG_THUC_MON (dinh muc NL cho mon)
-- ============================================================
CREATE TABLE cong_thuc_mon (
    cong_thuc_id    BIGSERIAL PRIMARY KEY,
    mon_id          INT NOT NULL,
    nguyen_lieu_id  INT NOT NULL,
    so_luong_can    DECIMAL(12,3) NOT NULL,

    CONSTRAINT fk_ct_mon FOREIGN KEY (mon_id) REFERENCES mon_an(mon_id),
    CONSTRAINT fk_ct_nl FOREIGN KEY (nguyen_lieu_id) REFERENCES nguyen_lieu(nguyen_lieu_id),
    CONSTRAINT uq_ct_mon_nl UNIQUE(mon_id, nguyen_lieu_id),
    CONSTRAINT ck_ct_so_luong CHECK (so_luong_can > 0)
);

CREATE INDEX idx_ct_mon ON cong_thuc_mon(mon_id);
CREATE INDEX idx_ct_nl ON cong_thuc_mon(nguyen_lieu_id);

-- ============================================================
-- 7) LICH_SU_KHO (nhap/xuat/dieu chinh kho)
-- ============================================================
CREATE TABLE lich_su_kho (
    lich_su_id      BIGSERIAL PRIMARY KEY,
    nguyen_lieu_id  INT NOT NULL,
    loai_gd         loai_giao_dich_kho_enum NOT NULL,
    so_luong        DECIMAL(12,3) NOT NULL,
    don_gia         DECIMAL(12,2) NULL,
    thanh_tien      DECIMAL(12,2) GENERATED ALWAYS AS (so_luong * COALESCE(don_gia, 0)) STORED,
    nguoi_thuc_hien BIGINT NOT NULL,
    ngay_giao_dich  TIMESTAMP(0) NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_lsk_nl FOREIGN KEY (nguyen_lieu_id) REFERENCES nguyen_lieu(nguyen_lieu_id),
    CONSTRAINT fk_lsk_user FOREIGN KEY (nguoi_thuc_hien) REFERENCES nguoi_dung(nguoi_dung_id),
    CONSTRAINT ck_lsk_so_luong CHECK (so_luong <> 0)
);

CREATE INDEX idx_lsk_nl ON lich_su_kho(nguyen_lieu_id);
CREATE INDEX idx_lsk_ngay ON lich_su_kho(ngay_giao_dich);
CREATE INDEX idx_lsk_loai ON lich_su_kho(loai_gd);

-- ============================================================
-- 8) DAT_BAN (Chi cho phep khach hang da dang ky)
-- ============================================================
CREATE TABLE dat_ban (
    dat_ban_id      BIGSERIAL PRIMARY KEY,
    ban_id          INT NOT NULL,
    khach_hang_id   BIGINT NOT NULL,
    so_nguoi        INT NOT NULL,
    thoi_gian_dat   TIMESTAMP(0) NOT NULL,
    ghi_chu         VARCHAR(500) NULL,
    trang_thai      trang_thai_dat_ban_enum NOT NULL DEFAULT 'cho_xac_nhan',
    tao_boi         BIGINT NOT NULL,
    ngay_tao        TIMESTAMP(0) NOT NULL DEFAULT NOW(),
    xac_nhan_boi    BIGINT NULL,
    ngay_xac_nhan   TIMESTAMP(0) NULL,

    CONSTRAINT fk_db_ban FOREIGN KEY (ban_id) REFERENCES ban_an(ban_id),
    CONSTRAINT fk_db_khach FOREIGN KEY (khach_hang_id) REFERENCES nguoi_dung(nguoi_dung_id),
    CONSTRAINT fk_db_tao_boi FOREIGN KEY (tao_boi) REFERENCES nguoi_dung(nguoi_dung_id),
    CONSTRAINT fk_db_xac_nhan_boi FOREIGN KEY (xac_nhan_boi) REFERENCES nguoi_dung(nguoi_dung_id),
    CONSTRAINT ck_db_so_nguoi CHECK (so_nguoi > 0),
    CONSTRAINT ck_db_thoi_gian CHECK (thoi_gian_dat >= CURRENT_DATE)
);

CREATE INDEX idx_db_ban ON dat_ban(ban_id);
CREATE INDEX idx_db_khach ON dat_ban(khach_hang_id);
CREATE INDEX idx_db_thoi_gian ON dat_ban(thoi_gian_dat);
CREATE INDEX idx_db_trang_thai ON dat_ban(trang_thai);

-- Tranh conflict khi dat ban cung thoi gian
CREATE UNIQUE INDEX uq_db_ban_thoi_gian
ON dat_ban(ban_id, thoi_gian_dat)
WHERE trang_thai IN ('cho_xac_nhan', 'da_xac_nhan');

-- ============================================================
-- 9) HOA_DON
-- ============================================================
CREATE TABLE hoa_don (
    hoa_don_id      BIGSERIAL PRIMARY KEY,
    ban_id          INT NOT NULL,
    khach_hang_id   BIGINT NULL,
    ma_hoa_don      VARCHAR(20) NOT NULL UNIQUE,
    tam_tinh        DECIMAL(12,2) NOT NULL DEFAULT 0,
    giam_gia        DECIMAL(12,2) NOT NULL DEFAULT 0,
    ly_do_giam_gia  VARCHAR(255) NULL,
    tong_tien       DECIMAL(12,2) NOT NULL DEFAULT 0,
    trang_thai      trang_thai_hoa_don_enum NOT NULL DEFAULT 'chua_thanh_toan',
    mo_boi          BIGINT NOT NULL,
    ngay_tao        TIMESTAMP(0) NOT NULL DEFAULT NOW(),
    ngay_dong       TIMESTAMP(0) NULL,
    row_version     INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_hd_ban FOREIGN KEY (ban_id) REFERENCES ban_an(ban_id),
    CONSTRAINT fk_hd_khach FOREIGN KEY (khach_hang_id) REFERENCES nguoi_dung(nguoi_dung_id),
    CONSTRAINT fk_hd_mo_boi FOREIGN KEY (mo_boi) REFERENCES nguoi_dung(nguoi_dung_id),
    CONSTRAINT ck_hd_tien CHECK (tam_tinh >= 0 AND giam_gia >= 0 AND tong_tien >= 0),
    CONSTRAINT ck_hd_giam_gia CHECK (giam_gia <= tam_tinh)
);

CREATE INDEX idx_hd_ban ON hoa_don(ban_id);
CREATE INDEX idx_hd_khach ON hoa_don(khach_hang_id);
CREATE INDEX idx_hd_ma ON hoa_don(ma_hoa_don);
CREATE INDEX idx_hd_trang_thai_ngay ON hoa_don(trang_thai, ngay_dong);
CREATE INDEX idx_hd_ngay_tao ON hoa_don(ngay_tao);

-- ============================================================
-- 10) DON_HANG
-- ============================================================
CREATE TABLE don_hang (
    don_hang_id     BIGSERIAL PRIMARY KEY,
    hoa_don_id      BIGINT NOT NULL,
    trang_thai      trang_thai_don_hang_enum NOT NULL DEFAULT 'cho_che_bien',
    tao_boi         BIGINT NOT NULL,
    ngay_tao        TIMESTAMP(0) NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_dh_hoa_don FOREIGN KEY (hoa_don_id) REFERENCES hoa_don(hoa_don_id),
    CONSTRAINT fk_dh_tao_boi FOREIGN KEY (tao_boi) REFERENCES nguoi_dung(nguoi_dung_id)
);

CREATE INDEX idx_dh_hoa_don ON don_hang(hoa_don_id);
CREATE INDEX idx_dh_trang_thai ON don_hang(trang_thai);
CREATE INDEX idx_dh_ngay_tao ON don_hang(ngay_tao);

-- ============================================================
-- 11) CHI_TIET_DON_HANG
-- ============================================================
CREATE TABLE chi_tiet_don_hang (
    chi_tiet_id     BIGSERIAL PRIMARY KEY,
    don_hang_id     BIGINT NOT NULL,
    mon_id          INT NOT NULL,
    so_luong        INT NOT NULL,
    don_gia         DECIMAL(12,2) NOT NULL,
    thanh_tien      DECIMAL(12,2) GENERATED ALWAYS AS (so_luong * don_gia) STORED,
    ghi_chu         VARCHAR(500) NULL,
    trang_thai      trang_thai_chi_tiet_enum NOT NULL DEFAULT 'cho_che_bien',

    CONSTRAINT fk_ctdh_don_hang FOREIGN KEY (don_hang_id) REFERENCES don_hang(don_hang_id),
    CONSTRAINT fk_ctdh_mon FOREIGN KEY (mon_id) REFERENCES mon_an(mon_id),
    CONSTRAINT ck_ctdh_so_luong CHECK (so_luong > 0),
    CONSTRAINT ck_ctdh_don_gia CHECK (don_gia >= 0)
);

CREATE INDEX idx_ctdh_don_hang ON chi_tiet_don_hang(don_hang_id);
CREATE INDEX idx_ctdh_mon ON chi_tiet_don_hang(mon_id);
CREATE INDEX idx_ctdh_trang_thai ON chi_tiet_don_hang(trang_thai);

-- ============================================================
-- 12) THANH_TOAN
-- ============================================================
CREATE TABLE thanh_toan (
    thanh_toan_id   BIGSERIAL PRIMARY KEY,
    hoa_don_id      BIGINT NOT NULL,
    so_tien         DECIMAL(12,2) NOT NULL,
    phuong_thuc     phuong_thuc_thanh_toan_enum NOT NULL,
    xu_ly_boi       BIGINT NOT NULL,
    ngay_thanh_toan TIMESTAMP(0) NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_tt_hd FOREIGN KEY (hoa_don_id) REFERENCES hoa_don(hoa_don_id),
    CONSTRAINT fk_tt_xu_ly FOREIGN KEY (xu_ly_boi) REFERENCES nguoi_dung(nguoi_dung_id),
    CONSTRAINT ck_tt_so_tien CHECK (so_tien > 0)
);

CREATE INDEX idx_tt_hoa_don ON thanh_toan(hoa_don_id);
CREATE INDEX idx_tt_ngay ON thanh_toan(ngay_thanh_toan);
CREATE INDEX idx_tt_phuong_thuc ON thanh_toan(phuong_thuc);

-- ============================================================
-- 13) AUDIT_LOG (Ghi lai lich su thay doi)
-- ============================================================
CREATE TABLE audit_log (
    log_id          BIGSERIAL PRIMARY KEY,
    table_name      VARCHAR(50) NOT NULL,
    record_id       BIGINT NOT NULL,
    action          audit_action_enum NOT NULL,
    old_values      JSONB NULL,
    new_values      JSONB NULL,
    nguoi_thuc_hien BIGINT NOT NULL,
    ngay_gio        TIMESTAMP(0) NOT NULL DEFAULT NOW(),
    ip_address      VARCHAR(50) NULL,

    CONSTRAINT fk_audit_user FOREIGN KEY (nguoi_thuc_hien) REFERENCES nguoi_dung(nguoi_dung_id)
);

CREATE INDEX idx_audit_table_record ON audit_log(table_name, record_id);
CREATE INDEX idx_audit_ngay ON audit_log(ngay_gio);
CREATE INDEX idx_audit_user ON audit_log(nguoi_thuc_hien);

-- ============================================================
-- TRIGGER FUNCTIONS
-- ============================================================

-- --------------------------------------------------------
-- Trigger: Cap nhat trang thai ban khi dat ban duoc xac nhan/huy
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_dat_ban_cap_nhat_ban()
RETURNS TRIGGER AS $$
BEGIN
    -- Chuyen sang da_dat khi xac nhan
    IF NEW.trang_thai = 'da_xac_nhan' THEN
        UPDATE ban_an
        SET trang_thai = 'da_dat'
        WHERE ban_id = NEW.ban_id
          AND trang_thai = 'trong'
          AND NOT EXISTS (
              SELECT 1 FROM dat_ban db
              WHERE db.ban_id = NEW.ban_id
                AND db.dat_ban_id <> NEW.dat_ban_id
                AND db.trang_thai = 'da_xac_nhan'
                AND db.thoi_gian_dat = NEW.thoi_gian_dat
          );
    END IF;

    -- Chuyen ve trong khi huy (neu khong con dat ban nao khac)
    IF NEW.trang_thai IN ('da_huy', 'khach_khong_den') THEN
        UPDATE ban_an
        SET trang_thai = 'trong'
        WHERE ban_id = NEW.ban_id
          AND NOT EXISTS (
              SELECT 1 FROM dat_ban db
              WHERE db.ban_id = NEW.ban_id
                AND db.trang_thai = 'da_xac_nhan'
          );
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_dat_ban_cap_nhat_ban
AFTER INSERT OR UPDATE ON dat_ban
FOR EACH ROW
EXECUTE FUNCTION fn_dat_ban_cap_nhat_ban();

-- --------------------------------------------------------
-- Trigger: Validate nguoi dung trong hoa don
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_hoa_don_validate()
RETURNS TRIGGER AS $$
DECLARE
    v_vai_tro vai_tro_enum;
    v_trang_thai trang_thai_nguoi_dung_enum;
BEGIN
    SELECT vai_tro, trang_thai INTO v_vai_tro, v_trang_thai
    FROM nguoi_dung
    WHERE nguoi_dung_id = NEW.mo_boi;

    IF v_vai_tro NOT IN ('QUAN_LY', 'NHAN_VIEN') OR v_trang_thai <> 'hoat_dong' THEN
        RAISE EXCEPTION 'hoa_don.mo_boi phai la nhan vien dang hoat dong.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_hoa_don_validate
BEFORE INSERT OR UPDATE ON hoa_don
FOR EACH ROW
EXECUTE FUNCTION fn_hoa_don_validate();

-- --------------------------------------------------------
-- Trigger: Auto-increment row_version cho hoa_don
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_hoa_don_row_version()
RETURNS TRIGGER AS $$
BEGIN
    NEW.row_version := OLD.row_version + 1;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_hoa_don_row_version
BEFORE UPDATE ON hoa_don
FOR EACH ROW
EXECUTE FUNCTION fn_hoa_don_row_version();

-- --------------------------------------------------------
-- Trigger: Validate nguoi dung trong don hang
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_don_hang_validate()
RETURNS TRIGGER AS $$
DECLARE
    v_vai_tro vai_tro_enum;
    v_trang_thai trang_thai_nguoi_dung_enum;
BEGIN
    SELECT vai_tro, trang_thai INTO v_vai_tro, v_trang_thai
    FROM nguoi_dung
    WHERE nguoi_dung_id = NEW.tao_boi;

    IF v_vai_tro NOT IN ('QUAN_LY', 'NHAN_VIEN') OR v_trang_thai <> 'hoat_dong' THEN
        RAISE EXCEPTION 'don_hang.tao_boi phai la nhan vien dang hoat dong.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_don_hang_validate
BEFORE INSERT OR UPDATE ON don_hang
FOR EACH ROW
EXECUTE FUNCTION fn_don_hang_validate();

-- --------------------------------------------------------
-- Trigger: Validate thanh toan
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_thanh_toan_validate()
RETURNS TRIGGER AS $$
DECLARE
    v_vai_tro vai_tro_enum;
    v_trang_thai trang_thai_nguoi_dung_enum;
    v_tong_da_tra DECIMAL(12,2);
    v_tong_tien DECIMAL(12,2);
BEGIN
    -- Kiem tra nguoi xu ly
    SELECT vai_tro, trang_thai INTO v_vai_tro, v_trang_thai
    FROM nguoi_dung
    WHERE nguoi_dung_id = NEW.xu_ly_boi;

    IF v_vai_tro NOT IN ('QUAN_LY', 'NHAN_VIEN') OR v_trang_thai <> 'hoat_dong' THEN
        RAISE EXCEPTION 'thanh_toan.xu_ly_boi phai la nhan vien dang hoat dong.';
    END IF;

    -- Kiem tra tong thu khong vuot tong_tien (dung SERIALIZABLE logic)
    SELECT tong_tien INTO v_tong_tien
    FROM hoa_don
    WHERE hoa_don_id = NEW.hoa_don_id
    FOR UPDATE; -- Row-level lock de tranh race condition

    SELECT COALESCE(SUM(so_tien), 0) + NEW.so_tien INTO v_tong_da_tra
    FROM thanh_toan
    WHERE hoa_don_id = NEW.hoa_don_id;

    IF v_tong_da_tra > v_tong_tien + 0.01 THEN
        RAISE EXCEPTION 'Thanh toan vuot tong tien hoa don. HD: %, Da tra: %, Tong tien: %',
            NEW.hoa_don_id, v_tong_da_tra, v_tong_tien;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_thanh_toan_validate
BEFORE INSERT ON thanh_toan
FOR EACH ROW
EXECUTE FUNCTION fn_thanh_toan_validate();

-- --------------------------------------------------------
-- Trigger: Cap nhat ton kho khi ghi lich su
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_lich_su_kho_cap_nhat_ton()
RETURNS TRIGGER AS $$
DECLARE
    v_ton_kho_moi DECIMAL(12,3);
BEGIN
    UPDATE nguyen_lieu
    SET ton_kho = CASE NEW.loai_gd
                    WHEN 'nhap' THEN ton_kho + NEW.so_luong
                    WHEN 'xuat' THEN ton_kho - NEW.so_luong
                    ELSE NEW.so_luong -- dieu_chinh: set truc tiep
                  END
    WHERE nguyen_lieu_id = NEW.nguyen_lieu_id
    RETURNING ton_kho INTO v_ton_kho_moi;

    IF v_ton_kho_moi < 0 THEN
        RAISE EXCEPTION 'Ton kho khong du de xuat. Nguyen lieu ID: %, Ton kho: %',
            NEW.nguyen_lieu_id, v_ton_kho_moi;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_lich_su_kho_cap_nhat_ton
AFTER INSERT ON lich_su_kho
FOR EACH ROW
EXECUTE FUNCTION fn_lich_su_kho_cap_nhat_ton();

-- --------------------------------------------------------
-- Trigger: Tu dong tinh lai hoa don (OPTIMIZED - KHONG DUNG CURSOR)
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_ctdh_tinh_lai_hd()
RETURNS TRIGGER AS $$
DECLARE
    v_don_hang_id BIGINT;
    v_hoa_don_id BIGINT;
    v_tam_tinh DECIMAL(12,2);
BEGIN
    -- Xac dinh don_hang_id tu NEW hoac OLD
    v_don_hang_id := COALESCE(NEW.don_hang_id, OLD.don_hang_id);

    -- Tim hoa_don_id tu don_hang
    SELECT hoa_don_id INTO v_hoa_don_id
    FROM don_hang
    WHERE don_hang_id = v_don_hang_id;

    IF v_hoa_don_id IS NULL THEN
        RETURN COALESCE(NEW, OLD);
    END IF;

    -- Tinh tong tam_tinh cho hoa don
    SELECT COALESCE(SUM(
        CASE WHEN ct.trang_thai::text <> 'huy' AND dh.trang_thai::text <> 'huy'
             THEN ct.thanh_tien ELSE 0 END
    ), 0)
    INTO v_tam_tinh
    FROM don_hang dh
    JOIN chi_tiet_don_hang ct ON ct.don_hang_id = dh.don_hang_id
    WHERE dh.hoa_don_id = v_hoa_don_id;

    -- Cap nhat hoa don
    UPDATE hoa_don
    SET tam_tinh = v_tam_tinh,
        tong_tien = v_tam_tinh - giam_gia
    WHERE hoa_don_id = v_hoa_don_id;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_ctdh_tinh_lai_hd
AFTER INSERT OR UPDATE OR DELETE ON chi_tiet_don_hang
FOR EACH ROW
EXECUTE FUNCTION fn_ctdh_tinh_lai_hd();

-- --------------------------------------------------------
-- Trigger: Audit log cho hoa don
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_hoa_don_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'UPDATE' THEN
        -- Chi ghi log khi trang_thai hoac tong_tien thay doi
        IF OLD.trang_thai <> NEW.trang_thai OR OLD.tong_tien <> NEW.tong_tien THEN
            INSERT INTO audit_log (table_name, record_id, action, old_values, new_values, nguoi_thuc_hien)
            VALUES (
                'hoa_don',
                NEW.hoa_don_id,
                'UPDATE',
                to_jsonb(OLD),
                to_jsonb(NEW),
                NEW.mo_boi
            );
        END IF;
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO audit_log (table_name, record_id, action, old_values, new_values, nguoi_thuc_hien)
        VALUES (
            'hoa_don',
            OLD.hoa_don_id,
            'DELETE',
            to_jsonb(OLD),
            NULL,
            OLD.mo_boi
        );
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_hoa_don_audit
AFTER UPDATE OR DELETE ON hoa_don
FOR EACH ROW
EXECUTE FUNCTION fn_hoa_don_audit();

-- --------------------------------------------------------
-- Trigger: Audit log cho lich su kho
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_lich_su_kho_audit()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO audit_log (table_name, record_id, action, new_values, nguoi_thuc_hien)
    VALUES (
        'lich_su_kho',
        NEW.lich_su_id,
        'INSERT',
        to_jsonb(NEW),
        NEW.nguoi_thuc_hien
    );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_lich_su_kho_audit
AFTER INSERT ON lich_su_kho
FOR EACH ROW
EXECUTE FUNCTION fn_lich_su_kho_audit();

-- --------------------------------------------------------
-- Trigger: Cap nhat trang thai thanh toan sau khi insert thanh_toan
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_thanh_toan_cap_nhat_trang_thai()
RETURNS TRIGGER AS $$
BEGIN
    PERFORM sp_cap_nhat_trang_thai_thanh_toan(NEW.hoa_don_id);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_thanh_toan_cap_nhat_trang_thai
AFTER INSERT ON thanh_toan
FOR EACH ROW
EXECUTE FUNCTION fn_thanh_toan_cap_nhat_trang_thai();

-- ============================================================
-- STORED PROCEDURES / FUNCTIONS
-- ============================================================

-- --------------------------------------------------------
-- Tao ma hoa don dang HD-yyyyMMdd-xxx
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION sp_tao_ma_hoa_don()
RETURNS VARCHAR(20) AS $$
DECLARE
    v_ngay VARCHAR(8);
    v_stt INT;
    v_ma_hoa_don VARCHAR(20);
BEGIN
    v_ngay := TO_CHAR(NOW(), 'YYYYMMDD');

    SELECT COALESCE(MAX(CAST(RIGHT(ma_hoa_don, 3) AS INT)), 0) + 1
    INTO v_stt
    FROM hoa_don
    WHERE ma_hoa_don LIKE 'HD-' || v_ngay || '-%';

    v_ma_hoa_don := 'HD-' || v_ngay || '-' || LPAD(v_stt::TEXT, 3, '0');

    RETURN v_ma_hoa_don;
END;
$$ LANGUAGE plpgsql;

-- --------------------------------------------------------
-- Tao nguoi dung voi hash mat khau
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION sp_tao_nguoi_dung(
    p_vai_tro VARCHAR(20),
    p_ho_ten VARCHAR(100),
    p_ten_dang_nhap VARCHAR(50),
    p_mat_khau VARCHAR(100),
    p_so_dien_thoai VARCHAR(20) DEFAULT NULL,
    p_email VARCHAR(100) DEFAULT NULL
)
RETURNS BIGINT AS $$
DECLARE
    v_salt UUID;
    v_mat_khau_hash VARCHAR(255);
    v_nguoi_dung_id BIGINT;
BEGIN
    -- Hash mat khau voi SHA-512 + salt
    v_salt := gen_random_uuid();
    v_mat_khau_hash := encode(
        digest(p_mat_khau || v_salt::TEXT, 'sha512'),
        'hex'
    );

    INSERT INTO nguoi_dung (vai_tro, ho_ten, ten_dang_nhap, mat_khau_hash, so_dien_thoai, email)
    VALUES (p_vai_tro::vai_tro_enum, p_ho_ten, p_ten_dang_nhap, v_mat_khau_hash, p_so_dien_thoai, p_email)
    RETURNING nguoi_dung_id INTO v_nguoi_dung_id;

    RETURN v_nguoi_dung_id;
END;
$$ LANGUAGE plpgsql;

-- --------------------------------------------------------
-- Cap nhat trang thai thanh toan hoa don
-- --------------------------------------------------------
CREATE OR REPLACE FUNCTION sp_cap_nhat_trang_thai_thanh_toan(
    p_hoa_don_id BIGINT
)
RETURNS VOID AS $$
DECLARE
    v_tong_tien DECIMAL(12,2);
    v_da_thanh_toan DECIMAL(12,2);
    v_trang_thai_moi trang_thai_hoa_don_enum;
BEGIN
    SELECT tong_tien INTO v_tong_tien
    FROM hoa_don
    WHERE hoa_don_id = p_hoa_don_id;

    SELECT COALESCE(SUM(so_tien), 0) INTO v_da_thanh_toan
    FROM thanh_toan
    WHERE hoa_don_id = p_hoa_don_id;

    -- Xac dinh trang thai moi
    IF v_da_thanh_toan >= v_tong_tien - 0.01 THEN
        v_trang_thai_moi := 'da_thanh_toan';
    ELSIF v_da_thanh_toan > 0 THEN
        v_trang_thai_moi := 'thanh_toan_mot_phan';
    ELSE
        v_trang_thai_moi := 'chua_thanh_toan';
    END IF;

    UPDATE hoa_don
    SET trang_thai = v_trang_thai_moi,
        ngay_dong = CASE
                        WHEN v_trang_thai_moi = 'da_thanh_toan' THEN NOW()
                        ELSE ngay_dong
                    END
    WHERE hoa_don_id = p_hoa_don_id;

    -- Cap nhat trang thai ban neu hoa don da thanh toan
    IF v_trang_thai_moi = 'da_thanh_toan' THEN
        UPDATE ban_an
        SET trang_thai = 'trong'
        FROM hoa_don hd
        WHERE hd.ban_id = ban_an.ban_id
          AND hd.hoa_don_id = p_hoa_don_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- ============================================================
-- VIEWS
-- ============================================================

-- --------------------------------------------------------
-- Bao cao doanh thu theo ngay
-- --------------------------------------------------------
CREATE OR REPLACE VIEW v_doanh_thu_ngay AS
SELECT
    CAST(hd.ngay_dong AS DATE) AS ngay,
    COUNT(DISTINCT hd.hoa_don_id) AS so_hoa_don,
    SUM(hd.tam_tinh) AS tong_tam_tinh,
    SUM(hd.giam_gia) AS tong_giam_gia,
    SUM(hd.tong_tien) AS doanh_thu
FROM hoa_don hd
WHERE hd.trang_thai = 'da_thanh_toan' AND hd.ngay_dong IS NOT NULL
GROUP BY CAST(hd.ngay_dong AS DATE);

-- --------------------------------------------------------
-- View: nguyen lieu can nhap
-- --------------------------------------------------------
CREATE OR REPLACE VIEW v_nguyen_lieu_sap_het AS
SELECT
    nguyen_lieu_id,
    ten_nguyen_lieu,
    don_vi_tinh,
    ton_kho,
    ton_kho_toi_thieu,
    (ton_kho_toi_thieu - ton_kho) AS can_nhap_them
FROM nguyen_lieu
WHERE ton_kho <= ton_kho_toi_thieu AND trang_thai = 'hoat_dong';

-- --------------------------------------------------------
-- View: Thong ke mon an ban chay
-- --------------------------------------------------------
CREATE OR REPLACE VIEW v_mon_an_ban_chay AS
SELECT
    ma.mon_id,
    ma.ten_mon,
    dm.ten_danh_muc,
    COUNT(ct.chi_tiet_id) AS so_lan_dat,
    COALESCE(SUM(ct.so_luong), 0) AS tong_so_luong,
    COALESCE(SUM(ct.thanh_tien), 0) AS tong_doanh_thu
FROM mon_an ma
JOIN danh_muc_mon dm ON dm.danh_muc_id = ma.danh_muc_id
LEFT JOIN chi_tiet_don_hang ct ON ct.mon_id = ma.mon_id AND ct.trang_thai <> 'huy'
LEFT JOIN don_hang dh ON dh.don_hang_id = ct.don_hang_id AND dh.trang_thai <> 'huy'
GROUP BY ma.mon_id, ma.ten_mon, dm.ten_danh_muc;

-- --------------------------------------------------------
-- View: Trang thai ban an
-- --------------------------------------------------------
CREATE OR REPLACE VIEW v_trang_thai_ban_an AS
SELECT
    ba.ban_id,
    ba.ma_ban,
    ba.suc_chua,
    ba.trang_thai,
    hd.hoa_don_id,
    hd.ma_hoa_don,
    hd.tong_tien,
    hd.ngay_tao AS gio_bat_dau_phuc_vu
FROM ban_an ba
LEFT JOIN hoa_don hd ON hd.ban_id = ba.ban_id
    AND hd.trang_thai IN ('chua_thanh_toan', 'thanh_toan_mot_phan');

-- --------------------------------------------------------
-- View: Lich su thanh toan
-- --------------------------------------------------------
CREATE OR REPLACE VIEW v_lich_su_thanh_toan AS
SELECT
    tt.thanh_toan_id,
    tt.hoa_don_id,
    hd.ma_hoa_don,
    ba.ma_ban,
    tt.so_tien,
    tt.phuong_thuc,
    nd.ho_ten AS nhan_vien_thu_tien,
    tt.ngay_thanh_toan
FROM thanh_toan tt
JOIN hoa_don hd ON hd.hoa_don_id = tt.hoa_don_id
JOIN ban_an ba ON ba.ban_id = hd.ban_id
JOIN nguoi_dung nd ON nd.nguoi_dung_id = tt.xu_ly_boi;

-- ============================================================
-- THONG BAO HOAN THANH
-- ============================================================
DO $$
BEGIN
    RAISE NOTICE '=============================================================';
    RAISE NOTICE 'Database PostgreSQL da duoc tao thanh cong!';
    RAISE NOTICE '';
    RAISE NOTICE 'CAC THAY DOI CHUYEN DOI TU SQL SERVER:';
    RAISE NOTICE '1.  IDENTITY -> SERIAL/BIGSERIAL';
    RAISE NOTICE '2.  DATETIME2(0) -> TIMESTAMP(0)';
    RAISE NOTICE '3.  NVARCHAR -> VARCHAR (PostgreSQL native UTF-8)';
    RAISE NOTICE '4.  SYSDATETIME() -> NOW()';
    RAISE NOTICE '5.  ROWVERSION -> INTEGER + auto-increment trigger';
    RAISE NOTICE '6.  CHECK IN(...) -> ENUM types';
    RAISE NOTICE '7.  THROW -> RAISE EXCEPTION';
    RAISE NOTICE '8.  FOR JSON PATH -> to_jsonb()';
    RAISE NOTICE '9.  HASHBYTES -> pgcrypto digest()';
    RAISE NOTICE '10. Filtered indexes -> Partial indexes (tuong tu)';
    RAISE NOTICE '11. CREATE OR ALTER TRIGGER -> Function + Trigger rieng';
    RAISE NOTICE '12. SET NOCOUNT ON -> Removed (khong can)';
    RAISE NOTICE '13. dbo. schema prefix -> Removed (dung public default)';
    RAISE NOTICE '14. GO batch separator -> Removed';
    RAISE NOTICE '15. Stored Procedures -> Functions (RETURNS void/value)';
    RAISE NOTICE '16. OUTPUT params -> RETURNS value';
    RAISE NOTICE '17. ISNULL -> COALESCE';
    RAISE NOTICE '18. Computed AS PERSISTED -> GENERATED ALWAYS AS STORED';
    RAISE NOTICE '19. Cursor trong trigger -> Replaced voi FOR EACH ROW';
    RAISE NOTICE '20. FOR UPDATE lock thay SERIALIZABLE trong trigger';
    RAISE NOTICE '=============================================================';
END;
$$;