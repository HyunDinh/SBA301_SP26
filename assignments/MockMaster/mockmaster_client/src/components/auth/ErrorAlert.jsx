export default function ErrorAlert({ message }) {
  return (
    <div className="bg-red-50 border-l-4 border-red-500 p-4 mb-4">
      <p className="text-sm text-red-700">{message}</p>
    </div>
  );
}