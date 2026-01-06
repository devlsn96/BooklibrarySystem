export async function generateOpenAIImage({ userApiKey, title, description }) {
  const prompt = `A book cover illustration for a book titled "${title}". 
  Description: ${description}. 
  Professional, clean, high-quality book cover design.`;

  try {
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
      throw new Error(`API 오류 : ${response.status}`);
    }
    const data = await response.json();
    console.log(data);
    return data.data[0].url;
  } catch (error) {
    console.error(`API 키 오류! ${error}`);
  }
}
