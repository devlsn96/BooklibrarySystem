export async function generateOpenAIImage({
  userApiKey,
  title,
  description,
}) {
  const prompt = `A book cover illustration for a book titled "${title}". 
  Description: ${description}. 
  Professional, clean, high-quality book cover design.`;

  const response = await fetch(
    "https://api.openai.com/v1/images/generations",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${userApiKey}`,
      },
      body: JSON.stringify({
        model: "dall-e-2",
        prompt,
        n: 1,
        size: "1024x1024",
      }),
    }
  );

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error?.error?.message || "이미지 생성 실패");
  }

  const data = await response.json();
  return data.data[0].url;
}