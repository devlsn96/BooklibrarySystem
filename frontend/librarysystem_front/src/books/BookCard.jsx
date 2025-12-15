import { Card, CardMedia, CardContent, Typography, Box, CardActionArea, CardActions } from '@mui/material';
import { useNavigate } from "react-router-dom";

function BookCard({ book }) {
  const navigate = useNavigate();
  const { bookNo, title, author, coverImageUrl, isLoaned } = book;

  return (
    <Card
      onClick={() => navigate(`/book/${bookNo}`)}
      sx={{
        maxWidth: 345,
        boxShadow: 5,
        cursor: 'pointer',
      }}
    >
      <CardActionArea>

        <CardMedia
          component="img"
          image={coverImageUrl}
          alt={title}
          sx={{ pr: 1.5, pb: 0, height: 140 }}
        />
        <CardContent sx={{ p: 1.5 }}>
          <Typography
            color='primary'
            variant="subtitle1"
            fontWeight="bold"
            textOverflow="ellipsis" 
            whiteSpace={"nowrap"}
            gutterBottom
            noWrap
            title={title}
          >
            {title}
          </Typography>
          <Typography variant="body2" color="textPrimary" noWrap>
            {author}
          </Typography>
        </CardContent>

      </CardActionArea>

      {/* 상태 텍스트 표시 */}
      <CardActions sx={{ width: '100%', textAlign: 'right' }} >
        <Typography
          size='small'
          variant="caption"
          fontWeight="bold"
          sx={{
            color: isLoaned ? 'error.main' : 'primary.main'
          }}
        >
          {isLoaned ? '대출 불가' : '대출 가능'}
        </Typography>
      </CardActions>
    </Card>
  );
}

export default BookCard;
