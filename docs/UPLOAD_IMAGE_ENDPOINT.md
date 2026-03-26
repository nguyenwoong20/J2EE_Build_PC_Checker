# Upload Image Endpoint

## Muc tieu
Tai lieu nay mo ta endpoint upload anh len Cloudinary va luong hoat dong khi luu duong dan vao truong imageUrl cua cac PC Component.

## Endpoint
- Method: POST
- URL: /api/files/upload
- Content-Type: multipart/form-data

### Form-data
- file: MultipartFile (bat buoc)
- entity: string (tuy chon, vi du: cpu, mainboard, ram, vga, ssd, hdd, psu, cooler, case)

## Phan hoi mau
```json
{
  "url": "https://res.cloudinary.com/<cloud-name>/image/upload/v1710000000/buildpcchecker/cpu/cpu-550e8400-e29b-41d4-a716-446655440000.jpg"
}
```

## Luong hoat dong
1. Client gui request POST /api/files/upload voi multipart/form-data, gom file va entity.
2. Server kiem tra file khong rong.
3. Server tao publicId theo format {entity}-{uuid}.
4. Server upload anh len Cloudinary theo folder: {app.cloudinary.folder}/{entity}.
5. Server tra ve URL public (secure_url) tu Cloudinary.
6. Khi tao/cap nhat PC Component, client luu URL nay vao truong imageUrl (chi luu URL, khong luu BLOB/Base64).

## Luu y cau hinh
- Cloudinary can cac bien moi truong: CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, CLOUDINARY_API_SECRET.
- Co the doi folder goc qua app.cloudinary.folder (mac dinh: buildpcchecker).
- Chi luu URL vao database, khong luu byte[] hoac Base64.
