export default function LoginHeader() {
  return (
    <div>
      <div className="flex justify-center">
        {/* Logo giả lập */}
        <div className="h-12 w-12 bg-blue-600 rounded-lg flex items-center justify-center text-white text-2xl font-bold shadow-lg">
          M
        </div>
      </div>
      <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
        MockMaster
      </h2>
      <p className="mt-2 text-center text-sm text-gray-600">
        Nền tảng giả lập API chuyên nghiệp
      </p>
    </div>
  );
}